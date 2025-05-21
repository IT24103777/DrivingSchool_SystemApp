<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.Driving_School_System.modules.admin_management.AdminModel" %>
<%
    List<AdminModel> admins = (List<AdminModel>) request.getAttribute("admins");
%>
<html>
<head>
    <title>Admin List</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.5.28/jspdf.plugin.autotable.min.js"></script>
</head>
<body class="bg-gray-50">
<div class="container mx-auto px-4 py-8">
    <div class="flex justify-between items-center mb-6">
        <h1 class="text-2xl font-bold text-gray-800">Admin Management</h1>
        <div class="flex space-x-4">
            <input type="text" id="searchInput" placeholder="Search admins..."
                   class="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500">
            <button onclick="generatePDF()"
                    class="px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700 transition">
                Export PDF
            </button>
            <a href="${pageContext.request.contextPath}/admin-create"
               class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition">
                Create New Admin
            </a>
        </div>
    </div>

    <div id="alert" class="hidden mb-6"></div>

    <div class="bg-white shadow rounded-lg overflow-hidden">
        <div class="overflow-x-auto">
            <table id="adminTable" class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                <tr>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Admin Number</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Contact</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                </tr>
                </thead>
                <tbody id="adminTableBody" class="bg-white divide-y divide-gray-200">
                <% for (AdminModel admin : admins) { %>
                <tr data-id="<%= admin.getId() %>" data-name="<%= admin.getName() %>"
                    data-number="<%= admin.getAdminNumber() %>" data-contact="<%= admin.getContactNumber() %>"
                    data-email="<%= admin.getEmail() %>" data-nic="<%= admin.getNic() %>">
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= admin.getId() %></td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900"><%= admin.getName() %></td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= admin.getAdminNumber() %></td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= admin.getContactNumber() %></td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <a href="${pageContext.request.contextPath}/admin-edit?id=<%= admin.getId() %>"
                           class="text-blue-600 hover:text-blue-900 mr-4">Edit</a>
                        <button onclick="confirmDelete(<%= admin.getId() %>)"
                                class="text-red-600 hover:text-red-900">Delete</button>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>

    const { jsPDF } = window.jspdf;


    async function fetchAdmins() {
        try {
            const response = await fetch('${pageContext.request.contextPath}/api/admins');
            if (!response.ok) throw new Error('Failed to fetch admins');
            return await response.json();
        } catch (error) {
            showAlert('Error loading admins: ' + error.message, 'error');
            return [];
        }
    }

    document.getElementById('searchInput').addEventListener('input', function() {
        const searchTerm = this.value.toLowerCase();
        const rows = document.querySelectorAll('#adminTableBody tr');

        rows.forEach(row => {
            const name = row.getAttribute('data-name').toLowerCase();
            const number = row.getAttribute('data-number').toLowerCase();
            const contact = row.getAttribute('data-contact').toLowerCase();
            const email = row.getAttribute('data-email').toLowerCase();

            if (name.includes(searchTerm) || number.includes(searchTerm) ||
                contact.includes(searchTerm) || email.includes(searchTerm)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    });


    function generatePDF() {
        const doc = new jsPDF();
        const table = document.getElementById('adminTable');
        const headers = [];
        const data = [];


        table.querySelectorAll('thead th').forEach(th => {
            headers.push(th.textContent.trim());
        });

        table.querySelectorAll('tbody tr').forEach(tr => {
            if (tr.style.display !== 'none') {
                const rowData = [];
                tr.querySelectorAll('td').forEach(td => {
                    rowData.push(td.textContent.trim());
                });
                data.push(rowData);
            }
        });

        doc.autoTable({
            head: [headers],
            body: data,
            theme: 'grid',
            styles: { fontSize: 8 },
            headStyles: { fillColor: [41, 128, 185] }
        });

        doc.save('admins-list.pdf');
    }

    async function confirmDelete(id) {
        if (confirm('Are you sure you want to delete this admin?')) {
            try {
                const response = await fetch('${pageContext.request.contextPath}/api/admins/' + id, {
                    method: 'DELETE'
                });

                if (response.ok) {
                    showAlert('Admin deleted successfully!', 'success');
                    setTimeout(() => {
                        window.location.reload();
                    }, 1500);
                } else {
                    const error = await response.json();
                    showAlert(error.error || 'Failed to delete admin', 'error');
                }
            } catch (error) {
                showAlert('Network error occurred. Please try again.', 'error');
            }
        }
    }

    function showAlert(message, type) {
        const alertDiv = document.getElementById('alert');
        alertDiv.innerHTML = '<div class="rounded-md p-4 ' + (type === 'success' ? 'bg-green-50' : 'bg-red-50') + '">' +
            '<div class="flex">' +
            '<div class="flex-shrink-0">' +
            '<svg class="h-5 w-5 ' + (type === 'success' ? 'text-green-400' : 'text-red-400') + '" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">' +
            '<path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />' +
            '</svg>' +
            '</div>' +
            '<div class="ml-3">' +
            '<p class="text-sm font-medium ' + (type === 'success' ? 'text-green-800' : 'text-red-800') + '">' +
            message +
            '</p>' +
            '</div>' +
            '</div>' +
            '</div>';
        alertDiv.classList.remove('hidden');
    }
</script>
</body>
</html>