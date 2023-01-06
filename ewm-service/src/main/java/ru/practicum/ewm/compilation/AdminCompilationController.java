package ru.practicum.ewm.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@Validated
public class AdminCompilationController {
    private final CompilationService compilationService;

    @Autowired
    public AdminCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.create(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.delete(compId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Long compId,
                                      @PathVariable Long eventId) {
        compilationService.addEvent(compId, eventId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId,
                                           @PathVariable Long eventId) {
        compilationService.deleteEvent(compId, eventId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilationOnMainPage(@PathVariable Long compId) {
        compilationService.pinOnMainPage(compId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilationFromMainPage(@PathVariable Long compId) {
        compilationService.unpinFromMainPage(compId);
    }
}
