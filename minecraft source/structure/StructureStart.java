/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  javax.annotation.Nullable
 *  org.slf4j.Logger
 */
package net.minecraft.structure;

import com.mojang.logging.LogUtils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePiecesList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.OceanMonumentStructure;
import net.minecraft.world.gen.structure.Structure;
import org.slf4j.Logger;

public final class StructureStart {
    public static final String INVALID = "INVALID";
    public static final StructureStart DEFAULT = new StructureStart(null, new ChunkPos(0, 0), 0, new StructurePiecesList(List.of()));
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Structure structure;
    private final StructurePiecesList children;
    private final ChunkPos pos;
    private int references;
    @Nullable
    private volatile BlockBox boundingBox;

    public StructureStart(Structure structure, ChunkPos pos, int references, StructurePiecesList children) {
        this.structure = structure;
        this.pos = pos;
        this.references = references;
        this.children = children;
    }

    @Nullable
    public static StructureStart fromNbt(StructureContext context, NbtCompound nbt, long seed) {
        String $$3 = nbt.getString("id", "");
        if (INVALID.equals($$3)) {
            return DEFAULT;
        }
        RegistryWrapper.Impl $$4 = context.registryManager().getOrThrow(RegistryKeys.STRUCTURE);
        Structure $$5 = (Structure)$$4.get(Identifier.of($$3));
        if ($$5 == null) {
            LOGGER.error("Unknown stucture id: {}", (Object)$$3);
            return null;
        }
        ChunkPos $$6 = new ChunkPos(nbt.getInt("ChunkX", 0), nbt.getInt("ChunkZ", 0));
        int $$7 = nbt.getInt("references", 0);
        NbtList $$8 = nbt.getListOrEmpty("Children");
        try {
            StructurePiecesList $$9 = StructurePiecesList.fromNbt($$8, context);
            if ($$5 instanceof OceanMonumentStructure) {
                $$9 = OceanMonumentStructure.modifyPiecesOnRead($$6, seed, $$9);
            }
            return new StructureStart($$5, $$6, $$7, $$9);
        }
        catch (Exception $$10) {
            LOGGER.error("Failed Start with id {}", (Object)$$3, (Object)$$10);
            return null;
        }
    }

    public BlockBox getBoundingBox() {
        BlockBox $$0 = this.boundingBox;
        if ($$0 == null) {
            this.boundingBox = $$0 = this.structure.expandBoxIfShouldAdaptNoise(this.children.getBoundingBox());
        }
        return $$0;
    }

    public void place(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos) {
        List<StructurePiece> $$6 = this.children.pieces();
        if ($$6.isEmpty()) {
            return;
        }
        BlockBox $$7 = $$6.get((int)0).boundingBox;
        BlockPos $$8 = $$7.getCenter();
        BlockPos $$9 = new BlockPos($$8.getX(), $$7.getMinY(), $$8.getZ());
        for (StructurePiece $$10 : $$6) {
            if (!$$10.getBoundingBox().intersects(chunkBox)) continue;
            $$10.generate(world, structureAccessor, chunkGenerator, random, chunkBox, chunkPos, $$9);
        }
        this.structure.postPlace(world, structureAccessor, chunkGenerator, random, chunkBox, chunkPos, this.children);
    }

    public NbtCompound toNbt(StructureContext context, ChunkPos chunkPos) {
        NbtCompound $$2 = new NbtCompound();
        if (!this.hasChildren()) {
            $$2.putString("id", INVALID);
            return $$2;
        }
        $$2.putString("id", context.registryManager().getOrThrow(RegistryKeys.STRUCTURE).getId(this.structure).toString());
        $$2.putInt("ChunkX", chunkPos.x);
        $$2.putInt("ChunkZ", chunkPos.z);
        $$2.putInt("references", this.references);
        $$2.put("Children", this.children.toNbt(context));
        return $$2;
    }

    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    public ChunkPos getPos() {
        return this.pos;
    }

    public boolean isNeverReferenced() {
        return this.references < this.getMinReferencedStructureReferenceCount();
    }

    public void incrementReferences() {
        ++this.references;
    }

    public int getReferences() {
        return this.references;
    }

    protected int getMinReferencedStructureReferenceCount() {
        return 1;
    }

    public Structure getStructure() {
        return this.structure;
    }

    public List<StructurePiece> getChildren() {
        return this.children.pieces();
    }
}

