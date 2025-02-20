package com.example.servlet;

import com.example.model.Category;
import com.example.model.MenuItem;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/menuItemServlet")
public class MenuItemServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));
        String category = request.getParameter("category");

        Category categoryObj = new Category(category);
        
        if (!isCategoryExist(category)) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                session.save(categoryObj);
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int categoryId = 0;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Integer> result = session.createQuery("SELECT id FROM Category WHERE name = :name", Integer.class)
                    .setParameter("name", category)
                    .getResultList();
            if (!result.isEmpty()) {
                categoryId = result.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        MenuItem menuItem = new MenuItem(name, description, price, categoryId);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(menuItem);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("menuItem");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MenuItem> menuItemList;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            menuItemList = session.createQuery("FROM MenuItem", MenuItem.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            menuItemList = List.of();
        }
        
        List<Category> categoryList;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            categoryList = session.createQuery("FROM Category", Category.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            categoryList = List.of();
        }

        request.setAttribute("menuItem", menuItemList);
        request.setAttribute("category", categoryList);
        request.getRequestDispatcher("menuItem.jsp").forward(request, response);
    }

    private boolean isCategoryExist(String category) {
        System.out.println("Category : " + category);
        List<Category> categoryList;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            categoryList = session.createQuery("FROM Category WHERE name=:name", Category.class)
                    .setParameter("name", category)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return !categoryList.isEmpty();
    }
}
