/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package net.minecraft.structure;

import javax.annotation.Nullable;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;

public interface StructurePiecesHolder {
    public void addPiece(StructurePiece var1);

    @Nullable
    public StructurePiece getIntersecting(BlockBox var1);
}

