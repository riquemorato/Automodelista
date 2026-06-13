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

// Exibe um toast temporário no canto superior direito
function showToast(message) {
  var container = document.querySelector('.toast-container');
  if (!container) return;

  var toast = document.createElement('div');
  toast.className = 'toast';
  toast.innerHTML = '<span>✅</span><span>' + message + '</span>';
  container.appendChild(toast);

  setTimeout(function () {
    toast.classList.add('toast--exit');
    setTimeout(function () { toast.remove(); }, 250);
  }, 3000);
}

// Dispara o toast de flash message (ex: após inscrição com sucesso)
document.addEventListener('DOMContentLoaded', function () {
  var trigger = document.getElementById('toast-trigger');
  if (trigger && trigger.dataset.message) {
    showToast(trigger.dataset.message);
  }
});