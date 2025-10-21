// Notes.js - Client-side enhancements for the notes page

function editNoteFromData(button) {
    const id = button.getAttribute('data-note-id');
    const title = button.getAttribute('data-note-title');
    const content = button.getAttribute('data-note-content');
    const color = button.getAttribute('data-note-color');
    editNote(id, title, content, color);
}

function editNote(id, title, content, color) {
    // Fill the form with the note's data
    document.getElementById('noteId').value = id;
    document.getElementById('title').value = title;
    document.getElementById('content').value = content;
    
    // Select the appropriate color radio button
    const colorInputs = document.querySelectorAll('input[name="color"]');
    colorInputs.forEach(input => {
        if (input.value === color.toString()) {
            input.checked = true;
        }
    });
    
    // Update form title and button text
    document.getElementById('form-title').textContent = 'Edit Note';
    document.getElementById('submitBtn').textContent = 'Update Note';
    
    // Show cancel button
    document.getElementById('cancelBtn').style.display = 'inline-block';
    
    // Scroll to form
    document.querySelector('.note-form-card').scrollIntoView({ behavior: 'smooth' });
}

function resetForm() {
    // Reset the form
    document.getElementById('noteForm').reset();
    document.getElementById('noteId').value = '';
    
    // Reset form title and button text
    document.getElementById('form-title').textContent = 'Create New Note';
    document.getElementById('submitBtn').textContent = 'Create Note';
    
    // Hide cancel button
    document.getElementById('cancelBtn').style.display = 'none';
}

function decodeHTML(html) {
    const txt = document.createElement('textarea');
    txt.innerHTML = html;
    return txt.value;
}

// Initialize - hide cancel button on page load
document.addEventListener('DOMContentLoaded', function() {
    const cancelBtn = document.getElementById('cancelBtn');
    if (cancelBtn && !document.getElementById('noteId').value) {
        cancelBtn.style.display = 'none';
    }
});
