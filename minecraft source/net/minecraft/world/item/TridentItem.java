/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
/*     */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*     */ import net.minecraft.world.item.component.Tool;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class TridentItem
/*     */   extends Item
/*     */   implements ProjectileItem
/*     */ {
/*     */   public static final int THROW_THRESHOLD_TIME = 10;
/*     */   public static final float BASE_DAMAGE = 8.0F;
/*     */   public static final float PROJECTILE_SHOOT_POWER = 2.5F;
/*     */   
/*  40 */   public TridentItem(Item.Properties properties) { super(properties); }
/*     */ 
/*     */ 
/*     */   
/*  44 */   public static ItemAttributeModifiers createAttributes() { return ItemAttributeModifiers.builder()
/*  45 */       .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 8.0D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/*  46 */       .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.9000000953674316D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/*  47 */       .build(); }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static Tool createToolProperties() { return new Tool(List.of(), 1.0F, 2, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public ItemUseAnimation getUseAnimation(ItemStack itemStack) { return ItemUseAnimation.TRIDENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public int getUseDuration(ItemStack itemStack, LivingEntity user) { return 72000; }
/*     */ 
/*     */   
/*     */   public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
/*     */     Player player;
/*  66 */     if (entity instanceof Player) { player = (Player)entity; }
/*  67 */     else { return false; }
/*     */ 
/*     */     
/*  70 */     int timeHeld = getUseDuration(itemStack, entity) - remainingTime;
/*  71 */     if (timeHeld < 10) {
/*  72 */       return false;
/*     */     }
/*     */     
/*  75 */     float riptideStrength = EnchantmentHelper.getTridentSpinAttackStrength(itemStack, player);
/*  76 */     if (riptideStrength > 0.0F && !player.isInWaterOrRain()) {
/*  77 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  81 */     if (itemStack.nextDamageWillBreak()) {
/*  82 */       return false;
/*     */     }
/*     */     
/*  85 */     Holder<SoundEvent> sound = (Holder)EnchantmentHelper.pickHighestLevel(itemStack, EnchantmentEffectComponents.TRIDENT_SOUND).orElse(SoundEvents.TRIDENT_THROW);
/*     */     
/*  87 */     player.awardStat(Stats.ITEM_USED.get(this));
/*     */     
/*  89 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  90 */       itemStack.hurtWithoutBreaking(1, player);
/*     */       
/*  92 */       if (riptideStrength == 0.0F) {
/*  93 */         ItemStack thrownItemStack = itemStack.consumeAndReturn(1, player);
/*  94 */         ThrownTrident trident = (ThrownTrident)Projectile.spawnProjectileFromRotation(ThrownTrident::new, serverLevel, thrownItemStack, player, 0.0F, 2.5F, 1.0F);
/*     */         
/*  96 */         if (player.hasInfiniteMaterials()) {
/*  97 */           trident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
/*     */         }
/*     */         
/* 100 */         level.playSound(null, trident, (SoundEvent)sound.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
/* 101 */         return true;
/*     */       }  }
/*     */ 
/*     */     
/* 105 */     if (riptideStrength > 0.0F) {
/* 106 */       float yRot = player.getYRot();
/* 107 */       float xRot = player.getXRot();
/*     */ 
/*     */       
/* 110 */       float xd = -Mth.sin((yRot * 0.017453292F)) * Mth.cos((xRot * 0.017453292F));
/* 111 */       float yd = -Mth.sin((xRot * 0.017453292F));
/* 112 */       float zd = Mth.cos((yRot * 0.017453292F)) * Mth.cos((xRot * 0.017453292F));
/* 113 */       float dist = Mth.sqrt(xd * xd + yd * yd + zd * zd);
/* 114 */       xd *= riptideStrength / dist;
/* 115 */       yd *= riptideStrength / dist;
/* 116 */       zd *= riptideStrength / dist;
/* 117 */       player.push(xd, yd, zd);
/*     */       
/* 119 */       player.startAutoSpinAttack(20, 8.0F, itemStack);
/* 120 */       if (player.onGround()) {
/* 121 */         float heightDifference = 1.1999999F;
/* 122 */         player.move(MoverType.SELF, new Vec3(0.0D, 1.1999999284744263D, 0.0D));
/*     */       } 
/*     */       
/* 125 */       level.playSound(null, player, (SoundEvent)sound.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
/* 126 */       return true;
/*     */     } 
/*     */     
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 134 */     ItemStack itemInHand = player.getItemInHand(hand);
/* 135 */     if (itemInHand.nextDamageWillBreak())
/*     */     {
/* 137 */       return InteractionResult.FAIL;
/*     */     }
/* 139 */     if (EnchantmentHelper.getTridentSpinAttackStrength(itemInHand, player) > 0.0F && !player.isInWaterOrRain())
/*     */     {
/* 141 */       return InteractionResult.FAIL;
/*     */     }
/* 143 */     player.startUsingItem(hand);
/* 144 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
/* 149 */     ThrownTrident trident = new ThrownTrident(level, position.x(), position.y(), position.z(), itemStack.copyWithCount(1));
/* 150 */     trident.pickup = AbstractArrow.Pickup.ALLOWED;
/*     */     
/* 152 */     return trident;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\TridentItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */