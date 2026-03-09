/*     */ package net.minecraft.world.entity.animal.armadillo;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.StringRepresentable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public static final abstract enum ArmadilloState
/*     */   implements StringRepresentable
/*     */ {
/*     */   IDLE, ROLLING, SCARED, UNROLLING;
/*     */   private static final Codec<ArmadilloState> CODEC;
/*     */   private static final IntFunction<ArmadilloState> BY_ID;
/*     */   public static final StreamCodec<ByteBuf, ArmadilloState> STREAM_CODEC;
/*     */   private final String name;
/*     */   private final boolean isThreatened;
/*     */   private final int animationDuration;
/*     */   private final int id;
/*     */   
/*     */   static  {
/*     */     // Byte code:
/*     */     //   0: new net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState$1
/*     */     //   3: dup
/*     */     //   4: ldc 'IDLE'
/*     */     //   6: iconst_0
/*     */     //   7: ldc 'idle'
/*     */     //   9: iconst_0
/*     */     //   10: iconst_0
/*     */     //   11: iconst_0
/*     */     //   12: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;ZII)V
/*     */     //   15: putstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.IDLE : Lnet/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState;
/*     */     //   18: new net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState$2
/*     */     //   21: dup
/*     */     //   22: ldc 'ROLLING'
/*     */     //   24: iconst_1
/*     */     //   25: ldc 'rolling'
/*     */     //   27: iconst_1
/*     */     //   28: bipush #10
/*     */     //   30: iconst_1
/*     */     //   31: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;ZII)V
/*     */     //   34: putstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.ROLLING : Lnet/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState;
/*     */     //   37: new net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState$3
/*     */     //   40: dup
/*     */     //   41: ldc 'SCARED'
/*     */     //   43: iconst_2
/*     */     //   44: ldc 'scared'
/*     */     //   46: iconst_1
/*     */     //   47: bipush #50
/*     */     //   49: iconst_2
/*     */     //   50: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;ZII)V
/*     */     //   53: putstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.SCARED : Lnet/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState;
/*     */     //   56: new net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState$4
/*     */     //   59: dup
/*     */     //   60: ldc 'UNROLLING'
/*     */     //   62: iconst_3
/*     */     //   63: ldc 'unrolling'
/*     */     //   65: iconst_1
/*     */     //   66: bipush #30
/*     */     //   68: iconst_3
/*     */     //   69: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;ZII)V
/*     */     //   72: putstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.UNROLLING : Lnet/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState;
/*     */     //   75: invokestatic $values : ()[Lnet/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState;
/*     */     //   78: putstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.$VALUES : [Lnet/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState;
/*     */     //   81: <illegal opcode> get : ()Ljava/util/function/Supplier;
/*     */     //   86: invokestatic fromEnum : (Ljava/util/function/Supplier;)Lnet/minecraft/util/StringRepresentable$EnumCodec;
/*     */     //   89: putstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.CODEC : Lcom/mojang/serialization/Codec;
/*     */     //   92: <illegal opcode> applyAsInt : ()Ljava/util/function/ToIntFunction;
/*     */     //   97: invokestatic values : ()[Lnet/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState;
/*     */     //   100: getstatic net/minecraft/util/ByIdMap$OutOfBoundsStrategy.ZERO : Lnet/minecraft/util/ByIdMap$OutOfBoundsStrategy;
/*     */     //   103: invokestatic continuous : (Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/ByIdMap$OutOfBoundsStrategy;)Ljava/util/function/IntFunction;
/*     */     //   106: putstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.BY_ID : Ljava/util/function/IntFunction;
/*     */     //   109: getstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.BY_ID : Ljava/util/function/IntFunction;
/*     */     //   112: <illegal opcode> applyAsInt : ()Ljava/util/function/ToIntFunction;
/*     */     //   117: invokestatic idMapper : (Ljava/util/function/IntFunction;Ljava/util/function/ToIntFunction;)Lnet/minecraft/network/codec/StreamCodec;
/*     */     //   120: putstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.STREAM_CODEC : Lnet/minecraft/network/codec/StreamCodec;
/*     */     //   123: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #402	-> 0
/*     */     //   #408	-> 18
/*     */     //   #414	-> 37
/*     */     //   #420	-> 56
/*     */     //   #400	-> 75
/*     */     //   #428	-> 81
/*     */     //   #429	-> 92
/*     */     //   #431	-> 109
/*     */   }
/*     */   
/*     */   ArmadilloState(String name, boolean isThreatened, int animationDuration, int id) {
/* 439 */     this.name = name;
/* 440 */     this.isThreatened = isThreatened;
/* 441 */     this.animationDuration = animationDuration;
/* 442 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 447 */   public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/* 451 */   private int id() { return this.id; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 457 */   public boolean isThreatened() { return this.isThreatened; }
/*     */ 
/*     */ 
/*     */   
/* 461 */   public int animationDuration() { return this.animationDuration; }
/*     */   
/*     */   public abstract boolean shouldHideInShell(long paramLong);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\armadillo\Armadillo$ArmadilloState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */