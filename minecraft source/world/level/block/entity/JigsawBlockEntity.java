/*     */ package net.minecraft.world.level.block.entity;
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.FrontAndTop;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.Pools;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.JigsawBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class JigsawBlockEntity extends BlockEntity {
/*  26 */   public static final Codec<ResourceKey<StructureTemplatePool>> POOL_CODEC = ResourceKey.codec(Registries.TEMPLATE_POOL); private static final int DEFAULT_PLACEMENT_PRIORITY = 0;
/*     */   private static final int DEFAULT_SELECTION_PRIORITY = 0;
/*  28 */   public static final Identifier EMPTY_ID = Identifier.withDefaultNamespace("empty"); public static final String TARGET = "target"; public static final String POOL = "pool"; public static final String JOINT = "joint"; public static final String PLACEMENT_PRIORITY = "placement_priority";
/*     */   public static final String SELECTION_PRIORITY = "selection_priority";
/*     */   public static final String NAME = "name";
/*     */   public static final String FINAL_STATE = "final_state";
/*     */   public static final String DEFAULT_FINAL_STATE = "minecraft:air";
/*     */   
/*  34 */   public enum JointType implements StringRepresentable { ROLLABLE("rollable"),
/*  35 */     ALIGNED("aligned"); public static final StringRepresentable.EnumCodec<JointType> CODEC; private final String name;
/*     */     static  {
/*  37 */       CODEC = StringRepresentable.fromEnum(JointType::values);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  42 */     JointType(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  47 */     public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/*  51 */     public Component getTranslatedName() { return Component.translatable("jigsaw_block.joint." + this.name); } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   private Identifier name = EMPTY_ID;
/*  70 */   private Identifier target = EMPTY_ID;
/*  71 */   private ResourceKey<StructureTemplatePool> pool = Pools.EMPTY;
/*  72 */   private JointType joint = JointType.ROLLABLE;
/*  73 */   private String finalState = "minecraft:air";
/*  74 */   private int placementPriority = 0;
/*  75 */   private int selectionPriority = 0;
/*     */ 
/*     */   
/*  78 */   public JigsawBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.JIGSAW, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */   
/*  82 */   public Identifier getName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/*  86 */   public Identifier getTarget() { return this.target; }
/*     */ 
/*     */ 
/*     */   
/*  90 */   public ResourceKey<StructureTemplatePool> getPool() { return this.pool; }
/*     */ 
/*     */ 
/*     */   
/*  94 */   public String getFinalState() { return this.finalState; }
/*     */ 
/*     */ 
/*     */   
/*  98 */   public JointType getJoint() { return this.joint; }
/*     */ 
/*     */ 
/*     */   
/* 102 */   public int getPlacementPriority() { return this.placementPriority; }
/*     */ 
/*     */ 
/*     */   
/* 106 */   public int getSelectionPriority() { return this.selectionPriority; }
/*     */ 
/*     */ 
/*     */   
/* 110 */   public void setName(Identifier name) { this.name = name; }
/*     */ 
/*     */ 
/*     */   
/* 114 */   public void setTarget(Identifier target) { this.target = target; }
/*     */ 
/*     */ 
/*     */   
/* 118 */   public void setPool(ResourceKey<StructureTemplatePool> pool) { this.pool = pool; }
/*     */ 
/*     */ 
/*     */   
/* 122 */   public void setFinalState(String finalState) { this.finalState = finalState; }
/*     */ 
/*     */ 
/*     */   
/* 126 */   public void setJoint(JointType joint) { this.joint = joint; }
/*     */ 
/*     */ 
/*     */   
/* 130 */   public void setPlacementPriority(int placementPriority) { this.placementPriority = placementPriority; }
/*     */ 
/*     */ 
/*     */   
/* 134 */   public void setSelectionPriority(int selectionPriority) { this.selectionPriority = selectionPriority; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 139 */     super.saveAdditional(output);
/* 140 */     output.store("name", Identifier.CODEC, this.name);
/* 141 */     output.store("target", Identifier.CODEC, this.target);
/* 142 */     output.store("pool", POOL_CODEC, this.pool);
/* 143 */     output.putString("final_state", this.finalState);
/* 144 */     output.store("joint", JointType.CODEC, this.joint);
/* 145 */     output.putInt("placement_priority", this.placementPriority);
/* 146 */     output.putInt("selection_priority", this.selectionPriority);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 151 */     super.loadAdditional(input);
/* 152 */     this.name = (Identifier)input.read("name", Identifier.CODEC).orElse(EMPTY_ID);
/* 153 */     this.target = (Identifier)input.read("target", Identifier.CODEC).orElse(EMPTY_ID);
/* 154 */     this.pool = (ResourceKey)input.read("pool", POOL_CODEC).orElse(Pools.EMPTY);
/* 155 */     this.finalState = input.getStringOr("final_state", "minecraft:air");
/* 156 */     this.joint = (JointType)input.read("joint", JointType.CODEC).orElseGet(() -> StructureTemplate.getDefaultJointType(getBlockState()));
/* 157 */     this.placementPriority = input.getIntOr("placement_priority", 0);
/* 158 */     this.selectionPriority = input.getIntOr("selection_priority", 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 163 */   public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 168 */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveCustomOnly(registries); }
/*     */ 
/*     */   
/*     */   public void generate(ServerLevel level, int levels, boolean keepJigsaws) {
/* 172 */     BlockPos position = getBlockPos().relative(((FrontAndTop)getBlockState().getValue(JigsawBlock.ORIENTATION)).front());
/*     */     
/* 174 */     Registry<StructureTemplatePool> poolRegistry = level.registryAccess().lookupOrThrow(Registries.TEMPLATE_POOL);
/* 175 */     Holder.Reference reference = poolRegistry.getOrThrow(this.pool);
/*     */     
/* 177 */     JigsawPlacement.generateJigsaw(level, reference, this.target, levels, position, keepJigsaws);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\JigsawBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */