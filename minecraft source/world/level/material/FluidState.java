/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public final class FluidState
/*     */   extends StateHolder<Fluid, FluidState>
/*     */ {
/*  30 */   public static final Codec<FluidState> CODEC = codec(BuiltInRegistries.FLUID.byNameCodec(), Fluid::defaultFluidState).stable();
/*     */   
/*     */   public static final int AMOUNT_MAX = 9;
/*     */   public static final int AMOUNT_FULL = 8;
/*     */   
/*  35 */   public FluidState(Fluid owner, Reference2ObjectArrayMap<Property<?>, Comparable<?>> values, MapCodec<FluidState> propertiesCodec) { super(owner, values, propertiesCodec); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public Fluid getType() { return (Fluid)this.owner; }
/*     */ 
/*     */ 
/*     */   
/*  46 */   public boolean isSource() { return getType().isSource(this); }
/*     */ 
/*     */ 
/*     */   
/*  50 */   public boolean isSourceOfType(Fluid fluidType) { return (this.owner == fluidType && ((Fluid)this.owner).isSource(this)); }
/*     */ 
/*     */ 
/*     */   
/*  54 */   public boolean isEmpty() { return getType().isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/*  58 */   public float getHeight(BlockGetter level, BlockPos pos) { return getType().getHeight(this, level, pos); }
/*     */ 
/*     */ 
/*     */   
/*  62 */   public float getOwnHeight() { return getType().getOwnHeight(this); }
/*     */ 
/*     */ 
/*     */   
/*  66 */   public int getAmount() { return getType().getAmount(this); }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderBackwardUpFace(BlockGetter level, BlockPos above) {
/*  70 */     for (int ox = -1; ox <= 1; ox++) {
/*  71 */       for (int oz = -1; oz <= 1; oz++) {
/*  72 */         BlockPos offset = above.offset(ox, 0, oz);
/*  73 */         FluidState fluidState = level.getFluidState(offset);
/*  74 */         if (!fluidState.getType().isSame(getType()) && !level.getBlockState(offset).isSolidRender()) {
/*  75 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*  79 */     return false;
/*     */   }
/*     */ 
/*     */   
/*  83 */   public void tick(ServerLevel level, BlockPos pos, BlockState blockState) { getType().tick(level, pos, blockState, this); }
/*     */ 
/*     */ 
/*     */   
/*  87 */   public void animateTick(Level level, BlockPos pos, RandomSource random) { getType().animateTick(level, pos, this, random); }
/*     */ 
/*     */ 
/*     */   
/*  91 */   public boolean isRandomlyTicking() { return getType().isRandomlyTicking(); }
/*     */ 
/*     */ 
/*     */   
/*  95 */   public void randomTick(ServerLevel level, BlockPos pos, RandomSource random) { getType().randomTick(level, pos, this, random); }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public Vec3 getFlow(BlockGetter level, BlockPos pos) { return getType().getFlow(level, pos, this); }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public BlockState createLegacyBlock() { return getType().createLegacyBlock(this); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public ParticleOptions getDripParticle() { return getType().getDripParticle(); }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public boolean is(TagKey<Fluid> tag) { return getType().builtInRegistryHolder().is(tag); }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public boolean is(HolderSet<Fluid> set) { return set.contains(getType().builtInRegistryHolder()); }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public boolean is(Fluid fluid) { return (getType() == fluid); }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public float getExplosionResistance() { return getType().getExplosionResistance(); }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public boolean canBeReplacedWith(BlockGetter level, BlockPos pos, Fluid other, Direction direction) { return getType().canBeReplacedWith(this, level, pos, other, direction); }
/*     */ 
/*     */ 
/*     */   
/* 131 */   public VoxelShape getShape(BlockGetter level, BlockPos pos) { return getType().getShape(this, level, pos); }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public AABB getAABB(BlockGetter level, BlockPos pos) { return getType().getAABB(this, level, pos); }
/*     */ 
/*     */ 
/*     */   
/* 139 */   public Holder<Fluid> holder() { return ((Fluid)this.owner).builtInRegistryHolder(); }
/*     */ 
/*     */ 
/*     */   
/* 143 */   public Stream<TagKey<Fluid>> getTags() { return ((Fluid)this.owner).builtInRegistryHolder().tags(); }
/*     */ 
/*     */ 
/*     */   
/* 147 */   public void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) { getType().entityInside(level, pos, entity, effectApplier); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\material\FluidState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */