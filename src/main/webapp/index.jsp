<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome to DrivePro School</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gradient-to-br from-blue-100 to-blue-300 min-h-screen flex flex-col">
<div class="flex-1 flex flex-col justify-center items-center text-center px-4">
    <h1 class="text-4xl md:text-6xl font-bold text-blue-800 mb-4">Welcome to DrivePro School</h1>
    <p class="text-lg md:text-2xl text-blue-700 mb-8 max-w-2xl">
        Your trusted partner for professional driving education. Learn from certified instructors, practice with modern vehicles, and get ready for the road!
    </p>
    <div class="flex flex-col md:flex-row gap-4 justify-center">

    </div>
</div>
<div class="bg-white py-10 shadow-inner">
    <div class="container mx-auto px-4">
        <h2 class="text-2xl font-bold text-blue-800 mb-4 text-center">Why Choose DrivePro?</h2>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8 text-center">
            <div class="p-6 bg-blue-50 rounded-lg shadow">
                <span class="text-4xl text-blue-600 mb-2 block">&#128663;</span>
                <h3 class="font-semibold text-lg mb-2">Modern Vehicles</h3>
                <p>Train with the latest, well-maintained vehicles for a safe learning experience.</p>
            </div>
            <div class="p-6 bg-blue-50 rounded-lg shadow">
                <span class="text-4xl text-blue-600 mb-2 block">&#128104;&#8205;&#127979;</span>
                <h3 class="font-semibold text-lg mb-2">Certified Instructors</h3>
                <p>Our experienced instructors ensure you learn the best driving practices.</p>
            </div>
            <div class="p-6 bg-blue-50 rounded-lg shadow">
                <span class="text-4xl text-blue-600 mb-2 block">&#128221;</span>
                <h3 class="font-semibold text-lg mb-2">Flexible Schedules</h3>
                <p>Book lessons at your convenience with our easy-to-use scheduling system.</p>
            </div>
        </div>
    </div>
</div>
<footer class="bg-blue-800 text-white text-center py-4 mt-8">
    &copy; <%= java.time.Year.now() %> DrivePro School. All rights reserved.
</footer>
</body>
</html>