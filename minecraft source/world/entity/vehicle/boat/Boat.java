/*    */ package net.minecraft.world.entity.vehicle.boat;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.world.entity.EntityDimensions;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Boat
/*    */   extends AbstractBoat
/*    */ {
/* 12 */   public Boat(EntityType<? extends Boat> type, Level level, Supplier<Item> dropItem) { super(type, level, dropItem); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   protected double rideHeight(EntityDimensions dimensions) { return (dimensions.height() / 3.0F); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\boat\Boat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */