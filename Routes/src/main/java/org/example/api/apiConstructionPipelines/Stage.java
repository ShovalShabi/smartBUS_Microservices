package org.example.api.apiConstructionPipelines;

import java.util.Collection;
import java.util.List;

public interface Stage<I, O> {
    O process(I input);
}
