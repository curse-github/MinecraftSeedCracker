/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends Slot
/*     */ {
/*  64 */   null(Container container, int slot, int x, int y) { super(container, slot, x, y); }
/*     */ 
/*     */   
/*  67 */   public boolean mayPlace(ItemStack itemStack) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTake(Player player, ItemStack carried) {
/*  72 */     access.execute((level, pos) -> {
/*  73 */           if (level instanceof ServerLevel) {
/*  74 */             ExperienceOrb.award((ServerLevel)level, Vec3.atCenterOf(pos), getExperienceAmount(level));
/*     */           }
/*  76 */           level.levelEvent(1042, pos, 0);
/*     */         });
/*     */     
/*  79 */     GrindstoneMenu.this.repairSlots.setItem(0, ItemStack.EMPTY);
/*  80 */     GrindstoneMenu.this.repairSlots.setItem(1, ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   private int getExperienceAmount(Level level) {
/*  84 */     int amount = 0;
/*  85 */     amount += getExperienceFromItem(GrindstoneMenu.this.repairSlots.getItem(0));
/*  86 */     amount += getExperienceFromItem(GrindstoneMenu.this.repairSlots.getItem(1));
/*     */     
/*  88 */     if (amount > 0) {
/*  89 */       int halfAmount = (int)Math.ceil(amount / 2.0D);
/*  90 */       return halfAmount + level.random.nextInt(halfAmount);
/*     */     } 
/*     */     
/*  93 */     return 0;
/*     */   }
/*     */   
/*     */   private int getExperienceFromItem(ItemStack item) {
/*  97 */     int amount = 0;
/*  98 */     ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(item);
/*  99 */     for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
/* 100 */       Holder<Enchantment> enchant = (Holder)entry.getKey();
/* 101 */       int lvl = entry.getIntValue();
/*     */       
/* 103 */       if (!enchant.is(EnchantmentTags.CURSE)) {
/* 104 */         amount += ((Enchantment)enchant.value()).getMinCost(lvl);
/*     */       }
/*     */     } 
/*     */     
/* 108 */     return amount;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\GrindstoneMenu$4.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */