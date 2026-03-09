/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.OptionalInt;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.dispenser.BlockSource;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public interface ProjectileItem
/*    */ {
/*    */   Projectile asProjectile(Level paramLevel, Position paramPosition, ItemStack paramItemStack, Direction paramDirection);
/*    */   
/* 17 */   default DispenseConfig createDispenseConfig() { return DispenseConfig.DEFAULT; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   default void shoot(Projectile projectile, double xd, double yd, double zd, float pow, float uncertainty) { projectile.shoot(xd, yd, zd, pow, uncertainty); }
/*    */   
/*    */   public static final class DispenseConfig extends Record {
/*    */     private final ProjectileItem.PositionFunction positionFunction;
/*    */     private final float uncertainty;
/*    */     private final float power;
/*    */     private final OptionalInt overrideDispenseEvent;
/*    */     
/* 29 */     public DispenseConfig(ProjectileItem.PositionFunction positionFunction, float uncertainty, float power, OptionalInt overrideDispenseEvent) { this.positionFunction = positionFunction; this.uncertainty = uncertainty; this.power = power; this.overrideDispenseEvent = overrideDispenseEvent; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/ProjectileItem$DispenseConfig;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/ProjectileItem$DispenseConfig; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/ProjectileItem$DispenseConfig;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/ProjectileItem$DispenseConfig; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/ProjectileItem$DispenseConfig;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/ProjectileItem$DispenseConfig;
/* 29 */       //   0	8	1	o	Ljava/lang/Object; } public ProjectileItem.PositionFunction positionFunction() { return this.positionFunction; } public float uncertainty() { return this.uncertainty; } public float power() { return this.power; } public OptionalInt overrideDispenseEvent() { return this.overrideDispenseEvent; }
/* 30 */     public static final DispenseConfig DEFAULT = builder().build();
/*    */ 
/*    */     
/* 33 */     public static Builder builder() { return new Builder(); }
/*    */     public static class Builder { private ProjectileItem.PositionFunction positionFunction; private float uncertainty; private float power; private OptionalInt overrideDispenseEvent;
/*    */       
/*    */       public Builder() {
/* 37 */         this.positionFunction = ((source, direction) -> DispenserBlock.getDispensePosition(source, 0.7D, new Vec3(0.0D, 0.1D, 0.0D)));
/* 38 */         this.uncertainty = 6.0F;
/* 39 */         this.power = 1.1F;
/* 40 */         this.overrideDispenseEvent = OptionalInt.empty();
/*    */       }
/*    */       public Builder positionFunction(ProjectileItem.PositionFunction positionFunction) {
/* 43 */         this.positionFunction = positionFunction;
/* 44 */         return this;
/*    */       }
/*    */       
/*    */       public Builder uncertainty(float uncertainty) {
/* 48 */         this.uncertainty = uncertainty;
/* 49 */         return this;
/*    */       }
/*    */       
/*    */       public Builder power(float power) {
/* 53 */         this.power = power;
/* 54 */         return this;
/*    */       }
/*    */       
/*    */       public Builder overrideDispenseEvent(int dispenseEvent) {
/* 58 */         this.overrideDispenseEvent = OptionalInt.of(dispenseEvent);
/* 59 */         return this;
/*    */       }
/*    */ 
/*    */       
/* 63 */       public ProjectileItem.DispenseConfig build() { return new ProjectileItem.DispenseConfig(this.positionFunction, this.uncertainty, this.power, this.overrideDispenseEvent); } } } @FunctionalInterface public static interface PositionFunction { Position getDispensePosition(BlockSource param1BlockSource, Direction param1Direction); } public static class Builder { public ProjectileItem.DispenseConfig build() { return new ProjectileItem.DispenseConfig(this.positionFunction, this.uncertainty, this.power, this.overrideDispenseEvent); }
/*    */     
/*    */     private ProjectileItem.PositionFunction positionFunction;
/*    */     private float uncertainty;
/*    */     private float power;
/*    */     private OptionalInt overrideDispenseEvent;
/*    */     
/*    */     public Builder() {
/*    */       this.positionFunction = ((source, direction) -> DispenserBlock.getDispensePosition(source, 0.7D, new Vec3(0.0D, 0.1D, 0.0D)));
/*    */       this.uncertainty = 6.0F;
/*    */       this.power = 1.1F;
/*    */       this.overrideDispenseEvent = OptionalInt.empty();
/*    */     }
/*    */     
/*    */     public Builder positionFunction(ProjectileItem.PositionFunction positionFunction) {
/*    */       this.positionFunction = positionFunction;
/*    */       return this;
/*    */     }
/*    */     
/*    */     public Builder uncertainty(float uncertainty) {
/*    */       this.uncertainty = uncertainty;
/*    */       return this;
/*    */     }
/*    */     
/*    */     public Builder power(float power) {
/*    */       this.power = power;
/*    */       return this;
/*    */     }
/*    */     
/*    */     public Builder overrideDispenseEvent(int dispenseEvent) {
/*    */       this.overrideDispenseEvent = OptionalInt.of(dispenseEvent);
/*    */       return this;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ProjectileItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */