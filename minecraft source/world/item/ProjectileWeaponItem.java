/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ProjectileWeaponItem
/*     */   extends Item
/*     */ {
/*  21 */   public static final Predicate<ItemStack> ARROW_ONLY = itemStack -> itemStack.is(ItemTags.ARROWS);
/*  22 */   public static final Predicate<ItemStack> ARROW_OR_FIREWORK = ARROW_ONLY.or(itemStack -> itemStack.is(Items.FIREWORK_ROCKET));
/*     */ 
/*     */   
/*  25 */   public ProjectileWeaponItem(Item.Properties properties) { super(properties); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  30 */   public Predicate<ItemStack> getSupportedHeldProjectiles() { return getAllSupportedProjectiles(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack getHeldProjectile(LivingEntity entity, Predicate<ItemStack> valid) {
/*  37 */     if (valid.test(entity.getItemInHand(InteractionHand.OFF_HAND))) {
/*  38 */       return entity.getItemInHand(InteractionHand.OFF_HAND);
/*     */     }
/*  40 */     if (valid.test(entity.getItemInHand(InteractionHand.MAIN_HAND))) {
/*  41 */       return entity.getItemInHand(InteractionHand.MAIN_HAND);
/*     */     }
/*  43 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void shoot(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectiles, float power, float uncertainty, boolean isCrit, LivingEntity targetOverride) {
/*  49 */     float maxAngle = EnchantmentHelper.processProjectileSpread(level, weapon, shooter, 0.0F);
/*  50 */     float angleStep = (projectiles.size() == 1) ? 0.0F : (2.0F * maxAngle / (projectiles.size() - 1));
/*  51 */     float angleOffset = ((projectiles.size() - 1) % 2) * angleStep / 2.0F;
/*  52 */     float direction = 1.0F;
/*  53 */     for (int i = 0; i < projectiles.size(); i++) {
/*  54 */       ItemStack projectile = (ItemStack)projectiles.get(i);
/*     */       
/*  56 */       if (!projectile.isEmpty()) {
/*     */ 
/*     */ 
/*     */         
/*  60 */         float angle = angleOffset + direction * ((i + 1) / 2) * angleStep;
/*  61 */         direction = -direction;
/*     */         
/*  63 */         int index = i;
/*     */         
/*  65 */         Projectile.spawnProjectile(
/*  66 */             createProjectile(level, shooter, weapon, projectile, isCrit), level, projectile, projectileEntity -> 
/*     */ 
/*     */             
/*  69 */             shootProjectile(shooter, projectileEntity, index, power, uncertainty, angle, targetOverride));
/*     */         
/*  71 */         weapon.hurtAndBreak(getDurabilityUse(projectile), shooter, hand.asEquipmentSlot());
/*  72 */         if (weapon.isEmpty()) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*  79 */   protected int getDurabilityUse(ItemStack projectile) { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack projectile, boolean isCrit) {
/*  85 */     Item item = projectile.getItem(); ArrowItem arrow = (ArrowItem)item, arrowItem = (item instanceof ArrowItem) ? arrow : (ArrowItem)Items.ARROW;
/*  86 */     AbstractArrow arrow = arrowItem.createArrow(level, projectile, shooter, weapon);
/*  87 */     if (isCrit) {
/*  88 */       arrow.setCritArrow(true);
/*     */     }
/*  90 */     return arrow;
/*     */   }
/*     */   
/*     */   protected static List<ItemStack> draw(ItemStack weapon, ItemStack projectile, LivingEntity shooter) {
/*  94 */     if (projectile.isEmpty()) {
/*  95 */       return List.of();
/*     */     }
/*     */ 
/*     */     
/*  99 */     Level level = shooter.level(); ServerLevel serverLevel = (ServerLevel)level; int numProjectiles = (level instanceof ServerLevel) ? EnchantmentHelper.processProjectileCount(serverLevel, weapon, shooter, 1) : 1;
/* 100 */     List<ItemStack> drawn = new ArrayList<ItemStack>(numProjectiles);
/*     */     
/* 102 */     ItemStack projectileCopy = projectile.copy();
/* 103 */     for (int i = 0; i < numProjectiles; i++) {
/* 104 */       ItemStack drawnStack = useAmmo(weapon, (i == 0) ? projectile : projectileCopy, shooter, (i > 0));
/* 105 */       if (!drawnStack.isEmpty()) {
/* 106 */         drawn.add(drawnStack);
/*     */       }
/*     */     } 
/*     */     
/* 110 */     return drawn;
/*     */   }
/*     */   
/*     */   protected static ItemStack useAmmo(ItemStack weapon, ItemStack projectile, LivingEntity holder, boolean forceInfinite) { // Byte code:
/*     */     //   0: iload_3
/*     */     //   1: ifne -> 43
/*     */     //   4: aload_2
/*     */     //   5: invokevirtual hasInfiniteMaterials : ()Z
/*     */     //   8: ifne -> 43
/*     */     //   11: aload_2
/*     */     //   12: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   15: astore #6
/*     */     //   17: aload #6
/*     */     //   19: instanceof net/minecraft/server/level/ServerLevel
/*     */     //   22: ifeq -> 43
/*     */     //   25: aload #6
/*     */     //   27: checkcast net/minecraft/server/level/ServerLevel
/*     */     //   30: astore #5
/*     */     //   32: aload #5
/*     */     //   34: aload_0
/*     */     //   35: aload_1
/*     */     //   36: iconst_1
/*     */     //   37: invokestatic processAmmoUse : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)I
/*     */     //   40: goto -> 44
/*     */     //   43: iconst_0
/*     */     //   44: istore #4
/*     */     //   46: iload #4
/*     */     //   48: aload_1
/*     */     //   49: invokevirtual getCount : ()I
/*     */     //   52: if_icmple -> 59
/*     */     //   55: getstatic net/minecraft/world/item/ItemStack.EMPTY : Lnet/minecraft/world/item/ItemStack;
/*     */     //   58: areturn
/*     */     //   59: iload #4
/*     */     //   61: ifne -> 86
/*     */     //   64: aload_1
/*     */     //   65: iconst_1
/*     */     //   66: invokevirtual copyWithCount : (I)Lnet/minecraft/world/item/ItemStack;
/*     */     //   69: astore #5
/*     */     //   71: aload #5
/*     */     //   73: getstatic net/minecraft/core/component/DataComponents.INTANGIBLE_PROJECTILE : Lnet/minecraft/core/component/DataComponentType;
/*     */     //   76: getstatic net/minecraft/util/Unit.INSTANCE : Lnet/minecraft/util/Unit;
/*     */     //   79: invokevirtual set : (Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   82: pop
/*     */     //   83: aload #5
/*     */     //   85: areturn
/*     */     //   86: aload_1
/*     */     //   87: iload #4
/*     */     //   89: invokevirtual split : (I)Lnet/minecraft/world/item/ItemStack;
/*     */     //   92: astore #5
/*     */     //   94: aload_1
/*     */     //   95: invokevirtual isEmpty : ()Z
/*     */     //   98: ifeq -> 123
/*     */     //   101: aload_2
/*     */     //   102: instanceof net/minecraft/world/entity/player/Player
/*     */     //   105: ifeq -> 123
/*     */     //   108: aload_2
/*     */     //   109: checkcast net/minecraft/world/entity/player/Player
/*     */     //   112: astore #6
/*     */     //   114: aload #6
/*     */     //   116: invokevirtual getInventory : ()Lnet/minecraft/world/entity/player/Inventory;
/*     */     //   119: aload_1
/*     */     //   120: invokevirtual removeItem : (Lnet/minecraft/world/item/ItemStack;)V
/*     */     //   123: aload #5
/*     */     //   125: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #115	-> 0
/*     */     //   #116	-> 46
/*     */     //   #117	-> 55
/*     */     //   #119	-> 59
/*     */     //   #120	-> 64
/*     */     //   #121	-> 71
/*     */     //   #122	-> 83
/*     */     //   #124	-> 86
/*     */     //   #125	-> 94
/*     */     //   #127	-> 114
/*     */     //   #129	-> 123
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   32	11	5	serverLevel	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   71	15	5	copy	Lnet/minecraft/world/item/ItemStack;
/*     */     //   114	9	6	player	Lnet/minecraft/world/entity/player/Player;
/*     */     //   0	126	0	weapon	Lnet/minecraft/world/item/ItemStack;
/*     */     //   0	126	1	projectile	Lnet/minecraft/world/item/ItemStack;
/*     */     //   0	126	2	holder	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   0	126	3	forceInfinite	Z
/*     */     //   46	80	4	ammoToUse	I
/*     */     //   94	32	5	used	Lnet/minecraft/world/item/ItemStack; }
/*     */   
/*     */   public abstract Predicate<ItemStack> getAllSupportedProjectiles();
/*     */   
/*     */   public abstract int getDefaultProjectileRange();
/*     */   
/*     */   protected abstract void shootProjectile(LivingEntity paramLivingEntity1, Projectile paramProjectile, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, LivingEntity paramLivingEntity2);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ProjectileWeaponItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */