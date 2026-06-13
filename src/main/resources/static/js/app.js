// Toggle da sidebar em telas pequenas (mobile)
function toggleSidebar() {
  document.querySelector('.sidebar').classList.toggle('sidebar--open');
  document.querySelector('.sidebar-backdrop').classList.toggle('show');
}