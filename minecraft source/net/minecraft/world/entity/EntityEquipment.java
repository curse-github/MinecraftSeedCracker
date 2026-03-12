/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.EnumMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class EntityEquipment
/*    */ {
/* 11 */   public static final Codec<EntityEquipment> CODEC = Codec.unboundedMap(EquipmentSlot.CODEC, ItemStack.CODEC).xmap(items -> {
/*    */         
/* 13 */         EnumMap<EquipmentSlot, ItemStack> map = new EnumMap<EquipmentSlot, ItemStack>(EquipmentSlot.class);
/* 14 */         map.putAll(items);
/* 15 */         return new EntityEquipment(map);
/*    */       }equipment -> {
/*    */         
/* 18 */         Map<EquipmentSlot, ItemStack> items = new EnumMap<EquipmentSlot, ItemStack>(equipment.items);
/* 19 */         items.values().removeIf(ItemStack::isEmpty);
/* 20 */         return items;
/*    */       });
/*    */ 
/*    */   
/*    */   private final EnumMap<EquipmentSlot, ItemStack> items;
/*    */ 
/*    */   
/* 27 */   private EntityEquipment(EnumMap<EquipmentSlot, ItemStack> items) { this.items = items; }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public EntityEquipment() { this(new EnumMap(EquipmentSlot.class)); }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public ItemStack set(EquipmentSlot slot, ItemStack itemStack) { return (ItemStack)Objects.requireNonNullElse((ItemStack)this.items.put(slot, itemStack), ItemStack.EMPTY); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public ItemStack get(EquipmentSlot slot) { return (ItemStack)this.items.getOrDefault(slot, ItemStack.EMPTY); }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 43 */     for (ItemStack item : this.items.values()) {
/* 44 */       if (!item.isEmpty()) {
/* 45 */         return false;
/*    */       }
/*    */     } 
/* 48 */     return true;
/*    */   }
/*    */   
/*    */   public void tick(Entity owner) {
/* 52 */     for (Map.Entry<EquipmentSlot, ItemStack> entry : this.items.entrySet()) {
/* 53 */       ItemStack item = (ItemStack)entry.getValue();
/* 54 */       if (!item.isEmpty()) {
/* 55 */         item.inventoryTick(owner.level(), owner, (EquipmentSlot)entry.getKey());
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setAll(EntityEquipment equipment) {
/* 61 */     this.items.clear();
/* 62 */     this.items.putAll(equipment.items);
/*    */   }
/*    */   
/*    */   public void dropAll(LivingEntity dropper) {
/* 66 */     for (ItemStack item : this.items.values()) {
/* 67 */       dropper.drop(item, true, false);
/*    */     }
/* 69 */     clear();
/*    */   }
/*    */ 
/*    */   
/* 73 */   public void clear() { this.items.replaceAll((s, v) -> ItemStack.EMPTY); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\EntityEquipment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */