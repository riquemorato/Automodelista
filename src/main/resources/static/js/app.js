// Toggle da sidebar em telas pequenas (mobile)
function toggleSidebar() {
  document.querySelector('.sidebar').classList.toggle('sidebar--open');
  document.querySelector('.sidebar-backdrop').classList.toggle('show');
}

// Destaca o item do menu lateral correspondente à página atual
document.addEventListener('DOMContentLoaded', function () {
  var path = window.location.pathname;
  document.querySelectorAll('.sidebar__link').forEach(function (link) {
    var href = link.getAttribute('href');
    if (href && href !== '/' && path.startsWith(href)) {
      link.classList.add('active');
    }
  });
});