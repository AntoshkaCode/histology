// Dynamic Navbar Auth State
function renderNavbar(auth) {
    console.log('[navbar-auth] Rendering navbar. Auth:', auth);
    const navbar = document.getElementById('navbar-auth');
    if (!navbar) {
        console.error('[navbar-auth] #navbar-auth element not found in DOM.');
        return;
    }
    if (auth && auth.authenticated === true) {
        // Format position: e.g., TECHNICIAN -> Technician
        let position = auth.position ? String(auth.position).toLowerCase().replace(/_/g, ' ').replace(/\b\w/g, c => c.toUpperCase()) : '';
        navbar.innerHTML = `
            <div class="d-flex align-items-center">
                <i class="bi bi-person-circle fs-3 me-2"></i>
                <span class="me-2">${auth.firstName ? auth.firstName : (auth.email ? auth.email : (auth.username || 'User'))}</span>
                <span class="badge bg-secondary me-3">${position}</span>
                <form action="/logout" method="post" class="d-inline mb-0">
                    <button type="submit" class="btn btn-outline-secondary btn-sm">Logout</button>
                </form>
            </div>
        `;
    } else {
        navbar.innerHTML = `
            <a href="/login" class="btn btn-outline-primary btn-sm me-2">Login</a>
            <a href="/register" class="btn btn-primary btn-sm">Register</a>
        `;
    }
}

function fetchAuthStatus() {
    console.log('[navbar-auth] Fetching /api/auth/status ...');
    fetch('/api/auth/status', {
        credentials: 'same-origin',
        headers: { 'Accept': 'application/json' }
    })
        .then(res => res.json())
        .then(data => {
            console.log('[navbar-auth] /api/auth/status response:', data);
            renderNavbar(data);
        })
        .catch((err) => {
            console.error('[navbar-auth] Error fetching /api/auth/status:', err);
            renderNavbar({ authenticated: false });
        });
}

document.addEventListener('DOMContentLoaded', fetchAuthStatus);
// Expose for manual refresh: window.fetchAuthStatus()
window.fetchAuthStatus = fetchAuthStatus;
// If your login/logout is AJAX, call window.fetchAuthStatus() after auth state changes.

