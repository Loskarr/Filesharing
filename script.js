document.getElementById("register-link").addEventListener("click", function (e) {
    e.preventDefault(); // Ngăn chuyển trang khi nhấn liên kết
    document.getElementById("form-style").href = "register.css"; // Thay đổi tệp CSS cho đăng ký
    document.getElementById("login-form").style.display = "none";
    document.getElementById("register-form").style.display = "block";
});

document.getElementById("login-link").addEventListener("click", function (e) {
    e.preventDefault(); // Ngăn chuyển trang khi nhấn liên kết
    document.getElementById("form-style").href = "login.css"; // Thay đổi tệp CSS cho đăng nhập
    document.getElementById("register-form").style.display = "none";
    document.getElementById("login-form").style.display = "block";
});
