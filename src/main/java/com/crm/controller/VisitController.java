package com.crm.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crm.dto.VisitDto;
import com.crm.service.VisitService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService service;

    // ✅ CREATE
    @PostMapping("/{customerId}")
    public ResponseEntity<VisitDto> create(@PathVariable Long customerId,
                                           @Valid @RequestBody VisitDto dto) {
        return ResponseEntity.ok(service.createVisit(customerId, dto));
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<VisitDto> update(@PathVariable Long id,
                                           @Valid @RequestBody VisitDto dto) {
        return ResponseEntity.ok(service.updateVisit(id, dto));
    }

    // 🔥 SOFT DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteVisit(id);
        return ResponseEntity.ok("Visit deleted successfully (Soft Delete)");
    }

    // ♻️ RESTORE
    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restore(@PathVariable Long id) {
        service.restoreVisit(id);
        return ResponseEntity.ok("Visit restored successfully");
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<VisitDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getVisitById(id));
    }

    // 🔥 GET ALL (Pagination + Sorting)
    @GetMapping
    public ResponseEntity<Page<VisitDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "visitDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(service.getAllVisits(page, size, sortBy, sortDir));
    }

    // 🔍 SEARCH by Customer Name/Contact
    @GetMapping("/search")
    public ResponseEntity<Page<VisitDto>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "visitDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(service.searchVisits(keyword, page, size, sortBy, sortDir));
    }
}