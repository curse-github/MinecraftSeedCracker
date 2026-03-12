/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Packed
/*    */   extends Record
/*    */ {
/*    */   private final boolean invulnerable;
/*    */   private final boolean flying;
/*    */   private final boolean mayFly;
/*    */   private final boolean instabuild;
/*    */   private final boolean mayBuild;
/*    */   private final float flyingSpeed;
/*    */   private final float walkingSpeed;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/Abilities$Packed;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/player/Abilities$Packed; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/Abilities$Packed;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/player/Abilities$Packed; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/player/Abilities$Packed;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/player/Abilities$Packed;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 61 */   public Packed(boolean invulnerable, boolean flying, boolean mayFly, boolean instabuild, boolean mayBuild, float flyingSpeed, float walkingSpeed) { this.invulnerable = invulnerable; this.flying = flying; this.mayFly = mayFly; this.instabuild = instabuild; this.mayBuild = mayBuild; this.flyingSpeed = flyingSpeed; this.walkingSpeed = walkingSpeed; } public boolean invulnerable() { return this.invulnerable; } public boolean flying() { return this.flying; } public boolean mayFly() { return this.mayFly; } public boolean instabuild() { return this.instabuild; } public boolean mayBuild() { return this.mayBuild; } public float flyingSpeed() { return this.flyingSpeed; } public float walkingSpeed() { return this.walkingSpeed; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 70 */   public static final Codec<Packed> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.BOOL
/*    */         
/* 72 */         .fieldOf("invulnerable").orElse(Boolean.valueOf(false)).forGetter(Packed::invulnerable), Codec.BOOL
/* 73 */         .fieldOf("flying").orElse(Boolean.valueOf(false)).forGetter(Packed::flying), Codec.BOOL
/* 74 */         .fieldOf("mayfly").orElse(Boolean.valueOf(false)).forGetter(Packed::mayFly), Codec.BOOL
/* 75 */         .fieldOf("instabuild").orElse(Boolean.valueOf(false)).forGetter(Packed::instabuild), Codec.BOOL
/* 76 */         .fieldOf("mayBuild").orElse(Boolean.valueOf(true)).forGetter(Packed::mayBuild), Codec.FLOAT
/* 77 */         .fieldOf("flySpeed").orElse(Float.valueOf(0.05F)).forGetter(Packed::flyingSpeed), Codec.FLOAT
/* 78 */         .fieldOf("walkSpeed").orElse(Float.valueOf(0.1F)).forGetter(Packed::walkingSpeed))
/* 79 */       .apply(i, Packed::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\player\Abilities$Packed.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */