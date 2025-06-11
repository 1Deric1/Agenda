package com.agenda.controller;

import com.agenda.Handler.DbException;
import com.agenda.model.DAO;
import com.agenda.model.JavaBeans;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(urlPatterns = {"/Controller", "/main", "/insert","/select","/update","/delete","/report"})
public class Controller extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public Controller() {
        super();
    }

    //Redirecionamento da pagina
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        String action = req.getServletPath();
        System.out.println(action);
        if (action.equals("/main")) {
            Contatos(req, resp);
        }
        else if (action.equals("/select")) {
            ListarContatos(req, resp);
        }
        else if (action.equals("/delete")) {
            Long id = Long.parseLong(req.getParameter("id"));
            deletarContatos(id);
            resp.sendRedirect("main");
        }

        else if (action.equals("/report")) {
            gerarRelatorio(req, resp);
        }
        else {
            resp.sendRedirect("index.html");
        }
    }

    private void gerarRelatorio(HttpServletRequest req, HttpServletResponse resp) {
        //Gerar um documento pdf como relatorio
    }


    //Reinserindo e Atualizar dados no forms
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        String action = req.getServletPath();
        if (action.equals("/insert")) {
            try {
                novoContato(req,resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            resp.getWriter().println("<h1>Dados inseridos com sucesso!</h1>");
        }
        else if (action.equals("/update")) {
            editarContato(req,resp);
            resp.getWriter().println("<h1>Dados atualizados com sucesso!</h1>");
        }
        else {
            resp.sendRedirect("index.html");
        }
    }

    public static void deletarContatos(Long id) {
            DAO.deletarContatos(id);
            System.out.println("Contato deletado com sucesso!");
    }


    //Editar contato
    private void editarContato(HttpServletRequest req, HttpServletResponse resp) {
        JavaBeans contato = new JavaBeans();
        try {
            contato.setId(Long.parseLong(req.getParameter("id")));
            contato.setNome(req.getParameter("nome"));
            contato.setTelefone(req.getParameter("telefone"));
            contato.setEmail(req.getParameter("email"));
            DAO.update(contato);
            resp.sendRedirect("main");
        } catch (DbException e) {
            throw new DbException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Criar e inserir novo contato
    private void novoContato(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        JavaBeans contato = new JavaBeans();
        try {
            contato.setNome(req.getParameter("nome"));
            contato.setTelefone(req.getParameter("telefone"));
            contato.setEmail(req.getParameter("email"));

            System.out.println("Nome: " + contato.getNome());
            System.out.println("Telefone: " + contato.getTelefone());
            System.out.println("Email: " + contato.getEmail());
            DAO.insert(contato);

        }catch (Exception e) {
            resp.getWriter().println("<h1>Erro ao inserir dados no banco:</h1>");
            resp.getWriter().println("<pre>" + e.getMessage() + "</pre>");
            e.printStackTrace();
        }
    }

    //Listar Contatos
    protected void Contatos(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ArrayList<JavaBeans> contacts = DAO.PrintAllContacts();
        try {
            req.setAttribute("contacts", contacts);
            req.getRequestDispatcher("agenda.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.getWriter().println("<h1>Erro ao carregar contatos:</h1>");
            resp.getWriter().println("<pre>" + e.getMessage() + "</pre>");
        }
    }

    //Editar Contato
    private void ListarContatos(HttpServletRequest req, HttpServletResponse resp) {
        JavaBeans contato = new JavaBeans();
        String id = req.getParameter("id");
        contato.setId(Long.parseLong(id));
        DAO.selecionarContatoPorId(contato.getId());
        req.setAttribute("contato", contato);
        req.setAttribute("id",contato.getId());
        req.setAttribute("nome",contato.getNome());
        req.setAttribute("telefone",contato.getTelefone());
        req.setAttribute("email",contato.getEmail());
        try {
            req.getRequestDispatcher("editar.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}