import App from "app";
import { Routes, Route, Navigate } from "react-router-dom";
import Auth from "screens/Auth/auth";
import Login from "screens/Auth/Login/login";
import CardForm from "screens/Card/CardForm/cardForm";
import Card from "screens/Card/card";
import NotFound from "screens/NotFound/notFound";
import Register from "./screens/Auth/Register/register";
import Home from "./screens/Home/home";
import IncomeForm from "screens/Income/IncomeForm/incomeForm";
import Income from "screens/Income/income";
import Expense from "screens/Expense/expense";
import ExpenseForm from "screens/Expense/ExpenseForm/expenseForm";
import UserSettings from "screens/UserSettings/userSettings";
import Category from "screens/Category/category";
import Invoice from "screens/Invoice/invoice";
import InvoiceView from "screens/Invoice/InvoiceView/invoiceView";

const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<App />}>
        <Route index element={<Home />} />
        <Route path="card" element={<Card />} />
        <Route path="card/add" element={<CardForm />} />
        <Route path="card/edit/:id" element={<CardForm />} />
        <Route path="income" element={<Income />} />
        <Route path="income/add" element={<IncomeForm />} />
        <Route path="income/edit/:id" element={<IncomeForm />} />
        <Route path="expense" element={<Expense />} />
        <Route path="expense/add" element={<ExpenseForm />} />
        <Route path="expense/edit/:id" element={<ExpenseForm />} />
        <Route path="settings" element={<UserSettings />} />
        <Route path="category" element={<Category />} />
        <Route path="card/:id/invoice" element={<Invoice />} />
        <Route
          path="card/:cardId/invoice/:invoiceId"
          element={<InvoiceView />}
        />
      </Route>
      <Route path="/auth" element={<Auth />}>
        <Route path="register" element={<Register />} />
        <Route path="login" element={<Login />} />
      </Route>
      <Route path="/login" element={<Navigate to="/auth/login" />} />
      <Route path="/register" element={<Navigate to="/auth/register" />} />
      <Route path="/user" element={<Navigate to="/settings" />} />
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};

export default AppRoutes;
