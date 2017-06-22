<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.io.BufferedWriter" %>
<%@page import="java.io.FileWriter" %>
<%@page import="java.util.Date" %>
<%@page import="java.util.Scanner" %>
<%@page import="main.java.org.grouplens.mooc.cbf.dao.*"%>
<%@page import="main.java.org.grouplens.mooc.cbf.*"%>
<%@page import="org.grouplens.lenskit.RecommenderBuildException.*" %>
<%
if ("POST".equalsIgnoreCase(request.getMethod())) {%>
<%= CBFMain.getRec(new String[] {request.getParameter("ident"), request.getParameter("model")})%>
<%
} else if ("GET".equalsIgnoreCase(request.getMethod())) {
%>
<%= CBFMain.getRec(new String[] {request.getParameter("ident"), request.getParameter("model")})%>
<%}else{%>
ERRO<%}%>


