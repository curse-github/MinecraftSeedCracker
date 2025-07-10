/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Lists
 *  com.mojang.logging.LogUtils
 *  org.slf4j.Logger
 */
package net.minecraft.structure;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;

public record StructurePiecesList(List<StructurePiece> pieces) {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Identifier JIGSAW = Identifier.ofVanilla("jigsaw");
    private static final Map<Identifier, Identifier> ID_UPDATES = ImmutableMap.builder().put((Object)Identifier.ofVanilla("nvi"), (Object)JIGSAW).put((Object)Identifier.ofVanilla("pcp"), (Object)JIGSAW).put((Object)Identifier.ofVanilla("bastionremnant"), (Object)JIGSAW).put((Object)Identifier.ofVanilla("runtime"), (Object)JIGSAW).build();

    public StructurePiecesList(List<StructurePiece> pieces) {
        this.pieces = List.copyOf(pieces);
    }

    public boolean isEmpty() {
        return this.pieces.isEmpty();
    }

    public boolean contains(BlockPos pos) {
        for (StructurePiece $$1 : this.pieces) {
            if (!$$1.getBoundingBox().contains(pos)) continue;
            return true;
        }
        return false;
    }

    public NbtElement toNbt(StructureContext context) {
        NbtList $$1 = new NbtList();
        for (StructurePiece $$2 : this.pieces) {
            $$1.add($$2.toNbt(context));
        }
        return $$1;
    }

    public static StructurePiecesList fromNbt(NbtList list, StructureContext context) {
        ArrayList $$2 = Lists.newArrayList();
        for (int $$3 = 0; $$3 < list.size(); ++$$3) {
            NbtCompound $$4 = list.getCompoundOrEmpty($$3);
            String $$5 = $$4.getString("id", "").toLowerCase(Locale.ROOT);
            Identifier $$6 = Identifier.of($$5);
            Identifier $$7 = ID_UPDATES.getOrDefault($$6, $$6);
            StructurePieceType $$8 = Registries.STRUCTURE_PIECE.get($$7);
            if ($$8 == null) {
                LOGGER.error("Unknown structure piece id: {}", (Object)$$7);
                continue;
            }
            try {
                StructurePiece $$9 = $$8.load(context, $$4);
                $$2.add($$9);
                continue;
            }
            catch (Exception $$10) {
                LOGGER.error("Exception loading structure piece with id {}", (Object)$$7, (Object)$$10);
            }
        }
        return new StructurePiecesList($$2);
    }

    public BlockBox getBoundingBox() {
        return StructurePiece.boundingBox(this.pieces.stream());
    }
}

