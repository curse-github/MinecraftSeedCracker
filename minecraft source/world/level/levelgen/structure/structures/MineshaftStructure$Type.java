/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
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
/*     */ public static enum Type
/*     */   implements StringRepresentable
/*     */ {
/*  81 */   NORMAL("normal", Blocks.OAK_LOG, Blocks.OAK_PLANKS, Blocks.OAK_FENCE),
/*  82 */   MESA("mesa", Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_FENCE); public static final Codec<Type> CODEC;
/*     */   
/*     */   static  {
/*  85 */     CODEC = StringRepresentable.fromEnum(Type::values);
/*  86 */     BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */   }
/*     */   private static final IntFunction<Type> BY_ID; private final String name;
/*     */   private final BlockState woodState;
/*     */   private final BlockState planksState;
/*     */   private final BlockState fenceState;
/*     */   
/*     */   Type(String name, Block wood, Block plank, Block fence) {
/*  94 */     this.name = name;
/*  95 */     this.woodState = wood.defaultBlockState();
/*  96 */     this.planksState = plank.defaultBlockState();
/*  97 */     this.fenceState = fence.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/* 101 */   public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public static Type byId(int id) { return (Type)BY_ID.apply(id); }
/*     */ 
/*     */ 
/*     */   
/* 109 */   public BlockState getWoodState() { return this.woodState; }
/*     */ 
/*     */ 
/*     */   
/* 113 */   public BlockState getPlanksState() { return this.planksState; }
/*     */ 
/*     */ 
/*     */   
/* 117 */   public BlockState getFenceState() { return this.fenceState; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public String getSerializedName() { return this.name; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\MineshaftStructure$Type.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */