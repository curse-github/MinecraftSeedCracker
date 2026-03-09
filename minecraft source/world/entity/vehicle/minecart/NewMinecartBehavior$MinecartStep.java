/*    */ package net.minecraft.world.entity.vehicle.minecart;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.phys.Vec3;
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
/*    */ public final class MinecartStep
/*    */   extends Record
/*    */ {
/*    */   private final Vec3 position;
/*    */   private final Vec3 movement;
/*    */   private final float yRot;
/*    */   private final float xRot;
/*    */   private final float weight;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 41 */   public MinecartStep(Vec3 position, Vec3 movement, float yRot, float xRot, float weight) { this.position = position; this.movement = movement; this.yRot = yRot; this.xRot = xRot; this.weight = weight; } public Vec3 position() { return this.position; } public Vec3 movement() { return this.movement; } public float yRot() { return this.yRot; } public float xRot() { return this.xRot; } public float weight() { return this.weight; }
/*    */   
/* 43 */   public static final StreamCodec<ByteBuf, MinecartStep> STREAM_CODEC = StreamCodec.composite(Vec3.STREAM_CODEC, MinecartStep::position, Vec3.STREAM_CODEC, MinecartStep::movement, ByteBufCodecs.ROTATION_BYTE, MinecartStep::yRot, ByteBufCodecs.ROTATION_BYTE, MinecartStep::xRot, ByteBufCodecs.FLOAT, MinecartStep::weight, MinecartStep::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public static MinecartStep ZERO = new MinecartStep(Vec3.ZERO, Vec3.ZERO, 0.0F, 0.0F, 0.0F);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\minecart\NewMinecartBehavior$MinecartStep.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */