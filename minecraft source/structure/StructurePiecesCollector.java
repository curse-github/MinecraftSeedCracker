/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  javax.annotation.Nullable
 */
package net.minecraft.structure;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.structure.StructurePiecesList;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.random.Random;

public class StructurePiecesCollector
implements StructurePiecesHolder {
    private final List<StructurePiece> pieces = Lists.newArrayList();

    @Override
    public void addPiece(StructurePiece piece) {
        this.pieces.add(piece);
    }

    @Override
    @Nullable
    public StructurePiece getIntersecting(BlockBox box) {
        return StructurePiece.firstIntersecting(this.pieces, box);
    }

    @Deprecated
    public void shift(int y) {
        for (StructurePiece $$1 : this.pieces) {
            $$1.translate(0, y, 0);
        }
    }

    @Deprecated
    public int shiftInto(int topY, int bottomY, Random random, int topPenalty) {
        int preferedTop = topY - topPenalty;
        BlockBox boundingBox = this.getBoundingBox();
        int yLevel = boundingBox.getBlockCountY() + bottomY + 1;
        if (yLevel < preferedTop) {
            yLevel += random.nextInt(preferedTop - yLevel);
        }
        int yFinal = yLevel - boundingBox.getMaxY();
        this.shift(yFinal);
        return yFinal;
    }

    public void shiftInto(Random random, int baseY, int topY) {
        int $$6;
        BlockBox structureBound = this.getBoundingBox();
        int $$4 = topY - baseY + 1 - structureBound.getBlockCountY();
        if ($$4 > 1) {
            int $$5 = baseY + random.nextInt($$4);
        } else {
            $$6 = baseY;
        }
        int $$7 = $$6 - structureBound.getMinY();
        this.shift($$7);
    }

    public StructurePiecesList toList() {
        return new StructurePiecesList(this.pieces);
    }

    public void clear() {
        this.pieces.clear();
    }

    public boolean isEmpty() {
        return this.pieces.isEmpty();
    }

    public BlockBox getBoundingBox() {
        return StructurePiece.boundingBox(this.pieces.stream());
    }
}

