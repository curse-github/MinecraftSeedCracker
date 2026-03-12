/*      */ package net.minecraft.world.level.block.state;
/*      */ 
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.MenuProvider;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.projectile.Projectile;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.context.BlockPlaceContext;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.EmptyBlockGetter;
/*      */ import net.minecraft.world.level.Explosion;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelAccessor;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.ScheduledTickAccess;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.EntityBlock;
/*      */ import net.minecraft.world.level.block.Mirror;
/*      */ import net.minecraft.world.level.block.RenderShape;
/*      */ import net.minecraft.world.level.block.Rotation;
/*      */ import net.minecraft.world.level.block.SoundType;
/*      */ import net.minecraft.world.level.block.SupportType;
/*      */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*      */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*      */ import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.material.Fluids;
/*      */ import net.minecraft.world.level.material.MapColor;
/*      */ import net.minecraft.world.level.material.PushReaction;
/*      */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*      */ import net.minecraft.world.level.redstone.Orientation;
/*      */ import net.minecraft.world.level.storage.loot.LootParams;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.CollisionContext;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class BlockStateBase
/*      */   extends StateHolder<Block, BlockState>
/*      */ {
/*  820 */   private static final Direction[] DIRECTIONS = Direction.values();
/*  821 */   private static final VoxelShape[] EMPTY_OCCLUSION_SHAPES = (VoxelShape[])Util.make(new VoxelShape[DIRECTIONS.length], s -> Arrays.fill(s, Shapes.empty()));
/*  822 */   private static final VoxelShape[] FULL_BLOCK_OCCLUSION_SHAPES = (VoxelShape[])Util.make(new VoxelShape[DIRECTIONS.length], s -> Arrays.fill(s, Shapes.block()));
/*      */   
/*      */   private final int lightEmission;
/*      */   
/*      */   private final boolean useShapeForLightOcclusion;
/*      */   private final boolean isAir;
/*      */   private final boolean ignitedByLava;
/*      */   @Deprecated
/*      */   private final boolean liquid;
/*      */   @Deprecated
/*      */   private boolean legacySolid;
/*      */   private final PushReaction pushReaction;
/*      */   private final MapColor mapColor;
/*      */   private final float destroySpeed;
/*      */   private final boolean requiresCorrectToolForDrops;
/*      */   private final boolean canOcclude;
/*      */   private final BlockBehaviour.StatePredicate isRedstoneConductor;
/*      */   private final BlockBehaviour.StatePredicate isSuffocating;
/*      */   private final BlockBehaviour.StatePredicate isViewBlocking;
/*      */   private final BlockBehaviour.StatePredicate hasPostProcess;
/*      */   private final BlockBehaviour.StatePredicate emissiveRendering;
/*      */   private final BlockBehaviour.OffsetFunction offsetFunction;
/*      */   private final boolean spawnTerrainParticles;
/*      */   private final NoteBlockInstrument instrument;
/*      */   private final boolean replaceable;
/*      */   private Cache cache;
/*  848 */   private FluidState fluidState = Fluids.EMPTY.defaultFluidState();
/*      */   private boolean isRandomlyTicking;
/*      */   private boolean solidRender;
/*      */   private VoxelShape occlusionShape;
/*      */   private VoxelShape[] occlusionShapesByFace;
/*      */   private boolean propagatesSkylightDown;
/*      */   private int lightBlock;
/*      */   
/*      */   protected BlockStateBase(Block owner, Reference2ObjectArrayMap<Property<?>, Comparable<?>> values, MapCodec<BlockState> propertiesCodec) {
/*  857 */     super(owner, values, propertiesCodec);
/*  858 */     BlockBehaviour.Properties properties = owner.properties;
/*      */     
/*  860 */     this.lightEmission = properties.lightEmission.applyAsInt(asState());
/*  861 */     this.useShapeForLightOcclusion = owner.useShapeForLightOcclusion(asState());
/*  862 */     this.isAir = properties.isAir;
/*  863 */     this.ignitedByLava = properties.ignitedByLava;
/*  864 */     this.liquid = properties.liquid;
/*  865 */     this.pushReaction = properties.pushReaction;
/*  866 */     this.mapColor = (MapColor)properties.mapColor.apply(asState());
/*  867 */     this.destroySpeed = properties.destroyTime;
/*  868 */     this.requiresCorrectToolForDrops = properties.requiresCorrectToolForDrops;
/*  869 */     this.canOcclude = properties.canOcclude;
/*  870 */     this.isRedstoneConductor = properties.isRedstoneConductor;
/*  871 */     this.isSuffocating = properties.isSuffocating;
/*  872 */     this.isViewBlocking = properties.isViewBlocking;
/*  873 */     this.hasPostProcess = properties.hasPostProcess;
/*  874 */     this.emissiveRendering = properties.emissiveRendering;
/*  875 */     this.offsetFunction = properties.offsetFunction;
/*  876 */     this.spawnTerrainParticles = properties.spawnTerrainParticles;
/*  877 */     this.instrument = properties.instrument;
/*  878 */     this.replaceable = properties.replaceable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean calculateSolid() {
/*  887 */     if (((Block)this.owner).properties.forceSolidOn) {
/*  888 */       return true;
/*      */     }
/*  890 */     if (((Block)this.owner).properties.forceSolidOff) {
/*  891 */       return false;
/*      */     }
/*  893 */     if (this.cache == null) {
/*  894 */       return false;
/*      */     }
/*  896 */     VoxelShape shape = this.cache.collisionShape;
/*  897 */     if (shape.isEmpty()) {
/*  898 */       return false;
/*      */     }
/*  900 */     AABB bounds = shape.bounds();
/*  901 */     if (bounds.getSize() >= 0.7291666666666666D) {
/*  902 */       return true;
/*      */     }
/*  904 */     if (bounds.getYsize() >= 1.0D) {
/*  905 */       return true;
/*      */     }
/*  907 */     return false;
/*      */   }
/*      */   
/*      */   public void initCache() {
/*  911 */     this.fluidState = ((Block)this.owner).getFluidState(asState());
/*  912 */     this.isRandomlyTicking = ((Block)this.owner).isRandomlyTicking(asState());
/*  913 */     if (!getBlock().hasDynamicShape()) {
/*  914 */       this.cache = new Cache(asState());
/*      */     }
/*  916 */     this.legacySolid = calculateSolid();
/*      */     
/*  918 */     this.occlusionShape = this.canOcclude ? ((Block)this.owner).getOcclusionShape(asState()) : Shapes.empty();
/*  919 */     this.solidRender = Block.isShapeFullBlock(this.occlusionShape);
/*      */     
/*  921 */     if (this.occlusionShape.isEmpty()) {
/*  922 */       this.occlusionShapesByFace = EMPTY_OCCLUSION_SHAPES;
/*  923 */     } else if (this.solidRender) {
/*  924 */       this.occlusionShapesByFace = FULL_BLOCK_OCCLUSION_SHAPES;
/*      */     } else {
/*  926 */       this.occlusionShapesByFace = new VoxelShape[DIRECTIONS.length];
/*  927 */       for (Direction direction : DIRECTIONS) {
/*  928 */         this.occlusionShapesByFace[direction.ordinal()] = this.occlusionShape.getFaceShape(direction);
/*      */       }
/*      */     } 
/*      */     
/*  932 */     this.propagatesSkylightDown = ((Block)this.owner).propagatesSkylightDown(asState());
/*  933 */     this.lightBlock = ((Block)this.owner).getLightBlock(asState());
/*      */   }
/*      */ 
/*      */   
/*  937 */   public Block getBlock() { return (Block)this.owner; }
/*      */ 
/*      */ 
/*      */   
/*  941 */   public Holder<Block> getBlockHolder() { return ((Block)this.owner).builtInRegistryHolder(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean blocksMotion() {
/*  957 */     Block block = getBlock();
/*  958 */     return (block != Blocks.COBWEB && block != Blocks.BAMBOO_SAPLING && isSolid());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  976 */   public boolean isSolid() { return this.legacySolid; }
/*      */ 
/*      */ 
/*      */   
/*  980 */   public boolean isValidSpawn(BlockGetter level, BlockPos pos, EntityType<?> type) { return (getBlock()).properties.isValidSpawn.test(asState(), level, pos, type); }
/*      */ 
/*      */ 
/*      */   
/*  984 */   public boolean propagatesSkylightDown() { return this.propagatesSkylightDown; }
/*      */ 
/*      */ 
/*      */   
/*  988 */   public int getLightBlock() { return this.lightBlock; }
/*      */ 
/*      */ 
/*      */   
/*  992 */   public VoxelShape getFaceOcclusionShape(Direction direction) { return this.occlusionShapesByFace[direction.ordinal()]; }
/*      */ 
/*      */ 
/*      */   
/*  996 */   public VoxelShape getOcclusionShape() { return this.occlusionShape; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1001 */   public boolean hasLargeCollisionShape() { return (this.cache == null || this.cache.largeCollisionShape); }
/*      */ 
/*      */ 
/*      */   
/* 1005 */   public boolean useShapeForLightOcclusion() { return this.useShapeForLightOcclusion; }
/*      */ 
/*      */ 
/*      */   
/* 1009 */   public int getLightEmission() { return this.lightEmission; }
/*      */ 
/*      */ 
/*      */   
/* 1013 */   public boolean isAir() { return this.isAir; }
/*      */ 
/*      */ 
/*      */   
/* 1017 */   public boolean ignitedByLava() { return this.ignitedByLava; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/* 1023 */   public boolean liquid() { return this.liquid; }
/*      */ 
/*      */ 
/*      */   
/* 1027 */   public MapColor getMapColor(BlockGetter level, BlockPos pos) { return this.mapColor; }
/*      */ 
/*      */ 
/*      */   
/* 1031 */   public BlockState rotate(Rotation rotation) { return getBlock().rotate(asState(), rotation); }
/*      */ 
/*      */ 
/*      */   
/* 1035 */   public BlockState mirror(Mirror mirror) { return getBlock().mirror(asState(), mirror); }
/*      */ 
/*      */ 
/*      */   
/* 1039 */   public RenderShape getRenderShape() { return getBlock().getRenderShape(asState()); }
/*      */ 
/*      */ 
/*      */   
/* 1043 */   public boolean emissiveRendering(BlockGetter level, BlockPos pos) { return this.emissiveRendering.test(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */   
/* 1047 */   public float getShadeBrightness(BlockGetter level, BlockPos pos) { return getBlock().getShadeBrightness(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */   
/* 1051 */   public boolean isRedstoneConductor(BlockGetter level, BlockPos pos) { return this.isRedstoneConductor.test(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */   
/* 1055 */   public boolean isSignalSource() { return getBlock().isSignalSource(asState()); }
/*      */ 
/*      */ 
/*      */   
/* 1059 */   public int getSignal(BlockGetter level, BlockPos pos, Direction direction) { return getBlock().getSignal(asState(), level, pos, direction); }
/*      */ 
/*      */ 
/*      */   
/* 1063 */   public boolean hasAnalogOutputSignal() { return getBlock().hasAnalogOutputSignal(asState()); }
/*      */ 
/*      */ 
/*      */   
/* 1067 */   public int getAnalogOutputSignal(Level level, BlockPos pos, Direction direction) { return getBlock().getAnalogOutputSignal(asState(), level, pos, direction); }
/*      */ 
/*      */ 
/*      */   
/* 1071 */   public float getDestroySpeed(BlockGetter level, BlockPos pos) { return this.destroySpeed; }
/*      */ 
/*      */ 
/*      */   
/* 1075 */   public float getDestroyProgress(Player player, BlockGetter level, BlockPos pos) { return getBlock().getDestroyProgress(asState(), player, level, pos); }
/*      */ 
/*      */ 
/*      */   
/* 1079 */   public int getDirectSignal(BlockGetter level, BlockPos pos, Direction direction) { return getBlock().getDirectSignal(asState(), level, pos, direction); }
/*      */ 
/*      */ 
/*      */   
/* 1083 */   public PushReaction getPistonPushReaction() { return this.pushReaction; }
/*      */ 
/*      */ 
/*      */   
/* 1087 */   public boolean isSolidRender() { return this.solidRender; }
/*      */ 
/*      */ 
/*      */   
/* 1091 */   public boolean canOcclude() { return this.canOcclude; }
/*      */ 
/*      */ 
/*      */   
/* 1095 */   public boolean skipRendering(BlockState neighborState, Direction direction) { return getBlock().skipRendering(asState(), neighborState, direction); }
/*      */ 
/*      */ 
/*      */   
/* 1099 */   public VoxelShape getShape(BlockGetter level, BlockPos pos) { return getShape(level, pos, CollisionContext.empty()); }
/*      */ 
/*      */ 
/*      */   
/* 1103 */   public VoxelShape getShape(BlockGetter level, BlockPos pos, CollisionContext context) { return getBlock().getShape(asState(), level, pos, context); }
/*      */ 
/*      */   
/*      */   public VoxelShape getCollisionShape(BlockGetter level, BlockPos pos) {
/* 1107 */     if (this.cache != null) {
/* 1108 */       return this.cache.collisionShape;
/*      */     }
/* 1110 */     return getCollisionShape(level, pos, CollisionContext.empty());
/*      */   }
/*      */ 
/*      */   
/* 1114 */   public VoxelShape getCollisionShape(BlockGetter level, BlockPos pos, CollisionContext context) { return getBlock().getCollisionShape(asState(), level, pos, context); }
/*      */ 
/*      */ 
/*      */   
/* 1118 */   public VoxelShape getEntityInsideCollisionShape(BlockGetter level, BlockPos pos, Entity entity) { return getBlock().getEntityInsideCollisionShape(asState(), level, pos, entity); }
/*      */ 
/*      */ 
/*      */   
/* 1122 */   public VoxelShape getBlockSupportShape(BlockGetter level, BlockPos pos) { return getBlock().getBlockSupportShape(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */   
/* 1126 */   public VoxelShape getVisualShape(BlockGetter level, BlockPos pos, CollisionContext context) { return getBlock().getVisualShape(asState(), level, pos, context); }
/*      */ 
/*      */ 
/*      */   
/* 1130 */   public VoxelShape getInteractionShape(BlockGetter level, BlockPos pos) { return getBlock().getInteractionShape(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */   
/* 1134 */   public final boolean entityCanStandOn(BlockGetter level, BlockPos pos, Entity entity) { return entityCanStandOnFace(level, pos, entity, Direction.UP); }
/*      */ 
/*      */ 
/*      */   
/* 1138 */   public final boolean entityCanStandOnFace(BlockGetter level, BlockPos pos, Entity entity, Direction faceDirection) { return Block.isFaceFull(getCollisionShape(level, pos, CollisionContext.of(entity)), faceDirection); }
/*      */ 
/*      */   
/*      */   public Vec3 getOffset(BlockPos pos) {
/* 1142 */     BlockBehaviour.OffsetFunction function = this.offsetFunction;
/* 1143 */     if (function != null) {
/* 1144 */       return function.evaluate(asState(), pos);
/*      */     }
/* 1146 */     return Vec3.ZERO;
/*      */   }
/*      */ 
/*      */   
/* 1150 */   public boolean hasOffsetFunction() { return (this.offsetFunction != null); }
/*      */ 
/*      */ 
/*      */   
/* 1154 */   public boolean triggerEvent(Level level, BlockPos pos, int b0, int b1) { return getBlock().triggerEvent(asState(), level, pos, b0, b1); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1162 */   public void handleNeighborChanged(Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) { getBlock().neighborChanged(asState(), level, pos, block, orientation, movedByPiston); }
/*      */ 
/*      */ 
/*      */   
/* 1166 */   public final void updateNeighbourShapes(LevelAccessor level, BlockPos pos, @UpdateFlags int updateFlags) { updateNeighbourShapes(level, pos, updateFlags, 512); }
/*      */ 
/*      */   
/*      */   public final void updateNeighbourShapes(LevelAccessor level, BlockPos pos, @UpdateFlags int updateFlags, int updateLimit) {
/* 1170 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 1171 */     for (Direction direction : BlockBehaviour.UPDATE_SHAPE_ORDER) {
/* 1172 */       blockPos.setWithOffset(pos, direction);
/* 1173 */       level.neighborShapeChanged(direction.getOpposite(), blockPos, pos, asState(), updateFlags, updateLimit);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 1178 */   public final void updateIndirectNeighbourShapes(LevelAccessor level, BlockPos pos, @UpdateFlags int updateFlags) { updateIndirectNeighbourShapes(level, pos, updateFlags, 512); }
/*      */ 
/*      */ 
/*      */   
/* 1182 */   public void updateIndirectNeighbourShapes(LevelAccessor level, BlockPos pos, @UpdateFlags int updateFlags, int updateLimit) { getBlock().updateIndirectNeighbourShapes(asState(), level, pos, updateFlags, updateLimit); }
/*      */ 
/*      */ 
/*      */   
/* 1186 */   public void onPlace(Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) { getBlock().onPlace(asState(), level, pos, oldState, movedByPiston); }
/*      */ 
/*      */ 
/*      */   
/* 1190 */   public void affectNeighborsAfterRemoval(ServerLevel level, BlockPos pos, boolean movedByPiston) { getBlock().affectNeighborsAfterRemoval(asState(), level, pos, movedByPiston); }
/*      */ 
/*      */ 
/*      */   
/* 1194 */   public void onExplosionHit(ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) { getBlock().onExplosionHit(asState(), level, pos, explosion, onHit); }
/*      */ 
/*      */ 
/*      */   
/* 1198 */   public void tick(ServerLevel level, BlockPos pos, RandomSource random) { getBlock().tick(asState(), level, pos, random); }
/*      */ 
/*      */ 
/*      */   
/* 1202 */   public void randomTick(ServerLevel level, BlockPos pos, RandomSource random) { getBlock().randomTick(asState(), level, pos, random); }
/*      */ 
/*      */ 
/*      */   
/* 1206 */   public void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) { getBlock().entityInside(asState(), level, pos, entity, effectApplier, isPrecise); }
/*      */ 
/*      */ 
/*      */   
/* 1210 */   public void spawnAfterBreak(ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) { getBlock().spawnAfterBreak(asState(), level, pos, tool, dropExperience); }
/*      */ 
/*      */ 
/*      */   
/* 1214 */   public List<ItemStack> getDrops(LootParams.Builder params) { return getBlock().getDrops(asState(), params); }
/*      */ 
/*      */ 
/*      */   
/* 1218 */   public InteractionResult useItemOn(ItemStack itemStack, Level level, Player player, InteractionHand hand, BlockHitResult hitResult) { return getBlock().useItemOn(itemStack, asState(), level, hitResult.getBlockPos(), player, hand, hitResult); }
/*      */ 
/*      */ 
/*      */   
/* 1222 */   public InteractionResult useWithoutItem(Level level, Player player, BlockHitResult hitResult) { return getBlock().useWithoutItem(asState(), level, hitResult.getBlockPos(), player, hitResult); }
/*      */ 
/*      */ 
/*      */   
/* 1226 */   public void attack(Level level, BlockPos pos, Player player) { getBlock().attack(asState(), level, pos, player); }
/*      */ 
/*      */ 
/*      */   
/* 1230 */   public boolean isSuffocating(BlockGetter level, BlockPos pos) { return this.isSuffocating.test(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */   
/* 1234 */   public boolean isViewBlocking(BlockGetter level, BlockPos pos) { return this.isViewBlocking.test(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */   
/* 1238 */   public BlockState updateShape(LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) { return getBlock().updateShape(asState(), level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random); }
/*      */ 
/*      */ 
/*      */   
/* 1242 */   public boolean isPathfindable(PathComputationType type) { return getBlock().isPathfindable(asState(), type); }
/*      */ 
/*      */ 
/*      */   
/* 1246 */   public boolean canBeReplaced(BlockPlaceContext context) { return getBlock().canBeReplaced(asState(), context); }
/*      */ 
/*      */ 
/*      */   
/* 1250 */   public boolean canBeReplaced(Fluid fluid) { return getBlock().canBeReplaced(asState(), fluid); }
/*      */ 
/*      */ 
/*      */   
/* 1254 */   public boolean canBeReplaced() { return this.replaceable; }
/*      */ 
/*      */ 
/*      */   
/* 1258 */   public boolean canSurvive(LevelReader level, BlockPos pos) { return getBlock().canSurvive(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */   
/* 1262 */   public boolean hasPostProcess(BlockGetter level, BlockPos pos) { return this.hasPostProcess.test(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */   
/* 1266 */   public MenuProvider getMenuProvider(Level level, BlockPos pos) { return getBlock().getMenuProvider(asState(), level, pos); }
/*      */ 
/*      */ 
/*      */   
/* 1270 */   public boolean is(TagKey<Block> tag) { return getBlock().builtInRegistryHolder().is(tag); }
/*      */ 
/*      */ 
/*      */   
/* 1274 */   public boolean is(TagKey<Block> tag, Predicate<BlockStateBase> predicate) { return (is(tag) && predicate.test(this)); }
/*      */ 
/*      */ 
/*      */   
/* 1278 */   public boolean is(HolderSet<Block> set) { return set.contains(getBlock().builtInRegistryHolder()); }
/*      */ 
/*      */ 
/*      */   
/* 1282 */   public boolean is(Holder<Block> holder) { return is((Block)holder.value()); }
/*      */ 
/*      */ 
/*      */   
/* 1286 */   public Stream<TagKey<Block>> getTags() { return getBlock().builtInRegistryHolder().tags(); }
/*      */ 
/*      */ 
/*      */   
/* 1290 */   public boolean hasBlockEntity() { return getBlock() instanceof EntityBlock; }
/*      */ 
/*      */ 
/*      */   
/* 1294 */   public boolean shouldChangedStateKeepBlockEntity(BlockState oldState) { return getBlock().shouldChangedStateKeepBlockEntity(oldState); }
/*      */ 
/*      */   
/*      */   public <T extends net.minecraft.world.level.block.entity.BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockEntityType<T> type) {
/* 1298 */     if (getBlock() instanceof EntityBlock) {
/* 1299 */       return ((EntityBlock)getBlock()).getTicker(level, asState(), type);
/*      */     }
/* 1301 */     return null;
/*      */   }
/*      */ 
/*      */   
/* 1305 */   public boolean is(Block block) { return (getBlock() == block); }
/*      */ 
/*      */ 
/*      */   
/* 1309 */   public boolean is(ResourceKey<Block> block) { return getBlock().builtInRegistryHolder().is(block); }
/*      */ 
/*      */ 
/*      */   
/* 1313 */   public FluidState getFluidState() { return this.fluidState; }
/*      */ 
/*      */ 
/*      */   
/* 1317 */   public boolean isRandomlyTicking() { return this.isRandomlyTicking; }
/*      */ 
/*      */ 
/*      */   
/* 1321 */   public long getSeed(BlockPos pos) { return getBlock().getSeed(asState(), pos); }
/*      */ 
/*      */ 
/*      */   
/* 1325 */   public SoundType getSoundType() { return getBlock().getSoundType(asState()); }
/*      */ 
/*      */ 
/*      */   
/* 1329 */   public void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile entity) { getBlock().onProjectileHit(level, state, blockHit, entity); }
/*      */ 
/*      */ 
/*      */   
/* 1333 */   public boolean isFaceSturdy(BlockGetter level, BlockPos pos, Direction direction) { return isFaceSturdy(level, pos, direction, SupportType.FULL); }
/*      */ 
/*      */   
/*      */   public boolean isFaceSturdy(BlockGetter level, BlockPos pos, Direction direction, SupportType supportType) {
/* 1337 */     if (this.cache != null) {
/* 1338 */       return this.cache.isFaceSturdy(direction, supportType);
/*      */     }
/* 1340 */     return supportType.isSupporting(asState(), level, pos, direction);
/*      */   }
/*      */   
/*      */   public boolean isCollisionShapeFullBlock(BlockGetter level, BlockPos pos) {
/* 1344 */     if (this.cache != null) {
/* 1345 */       return this.cache.isCollisionShapeFullBlock;
/*      */     }
/* 1347 */     return getBlock().isCollisionShapeFullBlock(asState(), level, pos);
/*      */   }
/*      */ 
/*      */   
/* 1351 */   public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, boolean includeData) { return getBlock().getCloneItemStack(level, pos, asState(), includeData); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1357 */   public boolean requiresCorrectToolForDrops() { return this.requiresCorrectToolForDrops; }
/*      */ 
/*      */ 
/*      */   
/* 1361 */   public boolean shouldSpawnTerrainParticles() { return this.spawnTerrainParticles; }
/*      */   
/*      */   protected abstract BlockState asState();
/*      */   
/* 1365 */   public NoteBlockInstrument instrument() { return this.instrument; }
/*      */   
/*      */   private static final class Cache
/*      */   {
/* 1369 */     private static final Direction[] DIRECTIONS = Direction.values();
/* 1370 */     private static final int SUPPORT_TYPE_COUNT = SupportType.values().length;
/*      */     protected final VoxelShape collisionShape;
/*      */     protected final boolean largeCollisionShape;
/*      */     private final boolean[] faceSturdy;
/*      */     protected final boolean isCollisionShapeFullBlock;
/*      */     
/*      */     private Cache(BlockState state) {
/* 1377 */       Block block = state.getBlock();
/*      */       
/* 1379 */       this.collisionShape = block.getCollisionShape(state, EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
/* 1380 */       if (!this.collisionShape.isEmpty() && state.hasOffsetFunction()) {
/* 1381 */         throw new IllegalStateException(String.format(Locale.ROOT, "%s has a collision shape and an offset type, but is not marked as dynamicShape in its properties.", new Object[] { BuiltInRegistries.BLOCK.getKey(block) }));
/*      */       }
/* 1383 */       this.largeCollisionShape = Arrays.stream(Direction.Axis.values()).anyMatch(axis -> (this.collisionShape.min(axis) < 0.0D || this.collisionShape.max(axis) > 1.0D));
/* 1384 */       this.faceSturdy = new boolean[DIRECTIONS.length * SUPPORT_TYPE_COUNT];
/* 1385 */       for (Direction direction : DIRECTIONS) {
/* 1386 */         for (SupportType type : SupportType.values()) {
/* 1387 */           this.faceSturdy[getFaceSupportIndex(direction, type)] = type.isSupporting(state, EmptyBlockGetter.INSTANCE, BlockPos.ZERO, direction);
/*      */         }
/*      */       } 
/* 1390 */       this.isCollisionShapeFullBlock = Block.isShapeFullBlock(state.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
/*      */     }
/*      */ 
/*      */     
/* 1394 */     public boolean isFaceSturdy(Direction direction, SupportType supportType) { return this.faceSturdy[getFaceSupportIndex(direction, supportType)]; }
/*      */ 
/*      */ 
/*      */     
/* 1398 */     private static int getFaceSupportIndex(Direction direction, SupportType supportType) { return direction.ordinal() * SUPPORT_TYPE_COUNT + supportType.ordinal(); }
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\BlockBehaviour$BlockStateBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */