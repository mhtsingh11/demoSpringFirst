package com.boot.first.demo.controlers;

import com.boot.first.demo.entity.JournalEntry;
import com.boot.first.demo.entity.UserEntity;
import com.boot.first.demo.service.JournalEntryService;
import com.boot.first.demo.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {


    private final JournalEntryService journalEntryService;
    private final UserService userService;

    public JournalEntryController(JournalEntryService journalEntryService, UserService userService) {
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getJournalEntries() {
        List<JournalEntry> allEntries = journalEntryService.getJournalEntries();
        if (!allEntries.isEmpty() && allEntries != null) {
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/findJournals/{username}")
    public ResponseEntity<?> getJournalEntriesForUser(@PathVariable String username) {
        Optional<UserEntity> userEntity = userService.findByName(username);
        List<JournalEntry> allEntries = userEntity.get().getJournalEntries();
        if (!allEntries.isEmpty() && allEntries != null) {
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create/{username}")
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journalEntry, @PathVariable String username) {
        try {
            journalEntryService.saveEntry(journalEntry, username);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId id) {
        Optional<JournalEntry> journalEntry = journalEntryService.getJournalEntry(id);
        return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{username}/{id}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable ObjectId id, @PathVariable String username) {
        journalEntryService.deleteJournalEntry(id, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{username}/{id}")
    public ResponseEntity<?> updateJournalEntry(@PathVariable ObjectId id, @PathVariable String username, @RequestBody JournalEntry journalEntry) {
        JournalEntry oldJournalEntry = journalEntryService.getJournalEntry(id).orElse(null);
        if (oldJournalEntry != null) {
            oldJournalEntry.setAuthor(journalEntry.getAuthor() != null && !journalEntry.getAuthor().isEmpty() ? journalEntry.getAuthor() : oldJournalEntry.getAuthor());
            oldJournalEntry.setContent(journalEntry.getContent() != null && !journalEntry.getContent().isEmpty() ? journalEntry.getContent() : oldJournalEntry.getContent());
            oldJournalEntry.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().isEmpty() ? journalEntry.getTitle() : oldJournalEntry.getTitle());
            journalEntryService.saveEntry(oldJournalEntry, username);
            return new ResponseEntity<>(oldJournalEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
