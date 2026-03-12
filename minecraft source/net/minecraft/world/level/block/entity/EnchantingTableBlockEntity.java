/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Nameable;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class EnchantingTableBlockEntity
/*     */   extends BlockEntity implements Nameable {
/*  20 */   private static final Component DEFAULT_NAME = Component.translatable("container.enchant");
/*     */   
/*     */   public int time;
/*     */   public float flip;
/*     */   public float oFlip;
/*     */   public float flipT;
/*     */   public float flipA;
/*     */   public float open;
/*     */   public float oOpen;
/*     */   public float rot;
/*     */   public float oRot;
/*     */   public float tRot;
/*  32 */   private static final RandomSource RANDOM = RandomSource.create();
/*     */   
/*     */   private Component name;
/*     */   
/*  36 */   public EnchantingTableBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.ENCHANTING_TABLE, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  41 */     super.saveAdditional(output);
/*  42 */     output.storeNullable("CustomName", ComponentSerialization.CODEC, this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  47 */     super.loadAdditional(input);
/*  48 */     this.name = parseCustomNameSafe(input, "CustomName");
/*     */   }
/*     */   
/*     */   public static void bookAnimationTick(Level level, BlockPos worldPosition, BlockState state, EnchantingTableBlockEntity entity) {
/*  52 */     entity.oOpen = entity.open;
/*  53 */     entity.oRot = entity.rot;
/*     */     
/*  55 */     Player player = level.getNearestPlayer(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, 3.0D, false);
/*  56 */     if (player != null) {
/*  57 */       double xd = player.getX() - worldPosition.getX() + 0.5D;
/*  58 */       double zd = player.getZ() - worldPosition.getZ() + 0.5D;
/*     */       
/*  60 */       entity.tRot = (float)Mth.atan2(zd, xd);
/*     */       
/*  62 */       entity.open += 0.1F;
/*     */       
/*  64 */       if (entity.open < 0.5F || RANDOM.nextInt(40) == 0) {
/*  65 */         float old = entity.flipT;
/*     */         do {
/*  67 */           entity.flipT += (RANDOM.nextInt(4) - RANDOM.nextInt(4));
/*  68 */         } while (old == entity.flipT);
/*     */       } 
/*     */     } else {
/*  71 */       entity.tRot += 0.02F;
/*  72 */       entity.open -= 0.1F;
/*     */     } 
/*     */     
/*  75 */     while (entity.rot >= 3.1415927F) {
/*  76 */       entity.rot -= 6.2831855F;
/*     */     }
/*  78 */     while (entity.rot < -3.1415927F) {
/*  79 */       entity.rot += 6.2831855F;
/*     */     }
/*  81 */     while (entity.tRot >= 3.1415927F) {
/*  82 */       entity.tRot -= 6.2831855F;
/*     */     }
/*  84 */     while (entity.tRot < -3.1415927F) {
/*  85 */       entity.tRot += 6.2831855F;
/*     */     }
/*  87 */     float rotDir = entity.tRot - entity.rot;
/*  88 */     while (rotDir >= 3.1415927F) {
/*  89 */       rotDir -= 6.2831855F;
/*     */     }
/*  91 */     while (rotDir < -3.1415927F) {
/*  92 */       rotDir += 6.2831855F;
/*     */     }
/*     */     
/*  95 */     entity.rot += rotDir * 0.4F;
/*     */     
/*  97 */     entity.open = Mth.clamp(entity.open, 0.0F, 1.0F);
/*     */     
/*  99 */     entity.time++;
/* 100 */     entity.oFlip = entity.flip;
/*     */     
/* 102 */     float diff = (entity.flipT - entity.flip) * 0.4F;
/* 103 */     float max = 0.2F;
/* 104 */     diff = Mth.clamp(diff, -0.2F, 0.2F);
/* 105 */     entity.flipA += (diff - entity.flipA) * 0.9F;
/*     */     
/* 107 */     entity.flip += entity.flipA;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getName() {
/* 112 */     if (this.name != null) {
/* 113 */       return this.name;
/*     */     }
/* 115 */     return DEFAULT_NAME;
/*     */   }
/*     */ 
/*     */   
/* 119 */   public void setCustomName(Component name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   public Component getCustomName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 129 */     super.applyImplicitComponents(components);
/* 130 */     this.name = (Component)components.get(DataComponents.CUSTOM_NAME);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/* 135 */     super.collectImplicitComponents(components);
/* 136 */     components.set(DataComponents.CUSTOM_NAME, this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 141 */   public void removeComponentsFromTag(ValueOutput output) { output.discard("CustomName"); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\EnchantingTableBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */