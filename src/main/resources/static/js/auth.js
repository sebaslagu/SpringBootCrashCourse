// Auth.js - Client-side enhancements for authentication pages

document.addEventListener('DOMContentLoaded', function() {
    // Add form validation feedback
    const forms = document.querySelectorAll('form');
    
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const submitBtn = form.querySelector('button[type="submit"]');
            if (submitBtn) {
                submitBtn.disabled = true;
                submitBtn.textContent = 'Processing...';
                
                // Re-enable after 3 seconds in case of network issues
                setTimeout(() => {
                    submitBtn.disabled = false;
                    submitBtn.textContent = submitBtn.dataset.originalText || submitBtn.textContent.replace('Processing...', 'Submit');
                }, 3000);
            }
        });
        
        // Store original button text
        const submitBtn = form.querySelector('button[type="submit"]');
        if (submitBtn && !submitBtn.dataset.originalText) {
            submitBtn.dataset.originalText = submitBtn.textContent;
        }
    });
    
    // Password strength indicator for register page
    const passwordInput = document.getElementById('password');
    if (passwordInput && window.location.pathname.includes('register')) {
        passwordInput.addEventListener('input', function() {
            const password = this.value;
            const strength = calculatePasswordStrength(password);
            updatePasswordStrengthUI(strength);
        });
    }
});

function calculatePasswordStrength(password) {
    let strength = 0;
    
    if (password.length >= 9) strength += 25;
    if (/[a-z]/.test(password)) strength += 25;
    if (/[A-Z]/.test(password)) strength += 25;
    if (/\d/.test(password)) strength += 25;
    
    return strength;
}

function updatePasswordStrengthUI(strength) {
    // Optional: Add a password strength indicator
    // This is a placeholder for future enhancement
    console.log('Password strength:', strength);
}
