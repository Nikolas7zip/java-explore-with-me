package ru.practicum.ewm.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.pagination.EntityPagination;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@Validated
public class PublicCompilationController {
    private final CompilationService compilationService;

    @Autowired
    public PublicCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        return compilationService.get(compId);
    }

    @GetMapping
    public List<CompilationDto> getAllCompilations(
            @RequestParam(name = "pinned", required = false) Boolean pinned,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        EntityPagination pagination = new EntityPagination(from, size, Sort.by("id").ascending());

        return compilationService.getAll(pinned, pagination);
    }
}
