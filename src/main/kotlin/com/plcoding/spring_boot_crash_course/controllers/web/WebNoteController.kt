package com.plcoding.spring_boot_crash_course.controllers.web

import com.plcoding.spring_boot_crash_course.database.model.Note
import com.plcoding.spring_boot_crash_course.database.repository.NoteRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.bson.types.ObjectId
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.time.Instant

@Controller
@RequestMapping("/web/notes")
class WebNoteController(
    private val noteRepository: NoteRepository
) {

    data class NoteForm(
        val id: String? = null,
        @field:NotBlank(message = "Title can't be blank.")
        val title: String = "",
        val content: String = "",
        val color: Long = 0xFFFFFFFF
    )

    @GetMapping
    fun listNotes(model: Model): String {
        val userId = SecurityContextHolder.getContext().authentication.principal as String
        val notes = noteRepository.findByOwnerId(ObjectId(userId))
        model.addAttribute("notes", notes)
        model.addAttribute("noteForm", NoteForm())
        return "notes"
    }

    @PostMapping
    fun saveNote(
        @Valid @ModelAttribute noteForm: NoteForm,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            val userId = SecurityContextHolder.getContext().authentication.principal as String
            val notes = noteRepository.findByOwnerId(ObjectId(userId))
            model.addAttribute("notes", notes)
            return "notes"
        }

        val userId = SecurityContextHolder.getContext().authentication.principal as String
        val note = Note(
            id = noteForm.id?.let { ObjectId(it) } ?: ObjectId.get(),
            title = noteForm.title,
            content = noteForm.content,
            color = noteForm.color,
            createdAt = Instant.now(),
            ownerId = ObjectId(userId)
        )
        noteRepository.save(note)
        
        return "redirect:/web/notes"
    }

    @PostMapping("/{id}/delete")
    fun deleteNote(@PathVariable id: String): String {
        val note = noteRepository.findById(ObjectId(id)).orElseThrow {
            IllegalArgumentException("Note not found")
        }
        val userId = SecurityContextHolder.getContext().authentication.principal as String
        if (note.ownerId.toHexString() == userId) {
            noteRepository.deleteById(ObjectId(id))
        }
        return "redirect:/web/notes"
    }
}
