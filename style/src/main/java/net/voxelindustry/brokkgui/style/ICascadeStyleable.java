package net.voxelindustry.brokkgui.style;

import net.voxelindustry.brokkgui.style.tree.StyleList;

import java.util.function.Supplier;

public interface ICascadeStyleable extends IStyleable
{
    ICascadeStyleable getParent();

    void setParent(ICascadeStyleable styleable);

    void setStyleTree(Supplier<StyleList> treeSupplier);
}
