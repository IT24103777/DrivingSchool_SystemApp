<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100">
<div class="container mx-auto py-12">
    <h1 class="text-3xl font-bold mb-8 text-center">Admin Dashboard</h1>
    <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <a href="admin-list" class="block bg-white rounded-lg shadow-lg p-8 hover:bg-blue-50 transition">
            <div class="flex flex-col items-center">
                <span class="text-4xl mb-4 text-blue-600">&#128100;</span>
                <h2 class="text-xl font-semibold mb-2">Admin Management</h2>
                <p class="text-gray-600 text-center">Manage admins, view, create, and edit admin records.</p>
            </div>
        </a>
        <a href="vehicle-list" class="block bg-white rounded-lg shadow-lg p-8 hover:bg-green-50 transition">
            <div class="flex flex-col items-center">
                <span class="text-4xl mb-4 text-green-600">&#128663;</span>
                <h2 class="text-xl font-semibold mb-2">Vehicle Management</h2>
                <p class="text-gray-600 text-center">Manage vehicles, view, create, and edit vehicle records.</p>
            </div>
        </a>
        <a href="instructor-list" class="block bg-white rounded-lg shadow-lg p-8 hover:bg-yellow-50 transition">
            <div class="flex flex-col items-center">
                <span class="text-4xl mb-4 text-yellow-600">&#128104;&#8205;&#127979;</span>
                <h2 class="text-xl font-semibold mb-2">Instructor Management</h2>
                <p class="text-gray-600 text-center">Manage instructors, view, create, and edit instructor records.</p>
            </div>
        </a>
        <a href="student-list" class="block bg-white rounded-lg shadow-lg p-8 hover:bg-purple-50 transition">
            <div class="flex flex-col items-center">
                <span class="text-4xl mb-4 text-purple-600">&#128104;&#8205;&#127891;</span>
                <h2 class="text-xl font-semibold mb-2">Student Management</h2>
                <p class="text-gray-600 text-center">Manage students, view, create, and edit student records.</p>
            </div>
        </a>
        <a href="schedule-list" class="block bg-white rounded-lg shadow-lg p-8 hover:bg-pink-50 transition">
            <div class="flex flex-col items-center">
                <span class="text-4xl mb-4 text-pink-600">&#128197;</span>
                <h2 class="text-xl font-semibold mb-2">Schedule Management</h2>
                <p class="text-gray-600 text-center">Manage schedules, view, create, and edit schedule records.</p>
            </div>
        </a>
        <a href="feedback-list" class="block bg-white rounded-lg shadow-lg p-8 hover:bg-red-50 transition">
            <div class="flex flex-col items-center">
                <span class="text-4xl mb-4 text-red-600">&#128172;</span>
                <h2 class="text-xl font-semibold mb-2">Feedback Management</h2>
                <p class="text-gray-600 text-center">Manage feedback, view, create, and edit feedback records.</p>
            </div>
        </a>
    </div>
</div>
</body>
</html>
