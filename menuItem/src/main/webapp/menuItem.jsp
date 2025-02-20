<%@page import="com.example.model.MenuItem"%>
<%@page import="com.example.model.Category"%>
<%@page import="java.util.List"%>
<html>
<head>
<title>Menuitem List</title>
</head>
<body>
	<h2>Menuitem List</h2>
	<form action="menuItem" method="post">
		Name: <input type="text" name="name" required> 
		Description: <input type="text" name="description" required>
		Price: <input type="number" name="price" required>
		Category: <input type="text" name="category" required> 
		<input type="submit" value="Add MenuItem">
	</form>

	<%
	List<MenuItem> menuItems = (List<MenuItem>) request.getAttribute("menuItem");
	if (menuItems == null) {
		menuItems = new java.util.ArrayList<>();
	}
	
	List<Category> categoryList = (List<Category>) request.getAttribute("category");
	if (categoryList == null) {
		categoryList = new java.util.ArrayList<>();
	}
	%>

	<table border="1">
		<tr>
			<th>Name</th>
			<th>Description</th>
			<th>Price</th>
			<th>Category</th>
		</tr>
		<%
		String category = "--";
		for (MenuItem item : menuItems) {
			
			for (Category itemCategory : categoryList) {
				
				if(item.getCategoryId() == itemCategory.getId()){
					category = itemCategory.getName();
					break;
				}
				
			}
		%>
		<tr>
			<td><%=item.getName()%></td>
			<td><%=item.getDescription()%></td>
			<td><%=item.getPrice()%></td>
			<td><%=category%></td>
		</tr>
		<%
		}
		%>
	</table>
</body>
</html>
