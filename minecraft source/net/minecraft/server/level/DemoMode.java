/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ 
/*     */ public class DemoMode
/*     */   extends ServerPlayerGameMode
/*     */ {
/*     */   public static final int DEMO_DAYS = 5;
/*     */   public static final int TOTAL_PLAY_TICKS = 120500;
/*     */   private boolean displayedIntro;
/*     */   private boolean demoHasEnded;
/*     */   private int demoEndedReminder;
/*     */   private int gameModeTicks;
/*     */   
/*  25 */   public DemoMode(ServerPlayer player) { super(player); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  30 */     super.tick();
/*  31 */     this.gameModeTicks++;
/*     */     
/*  33 */     long time = this.level.getGameTime();
/*  34 */     long day = time / 24000L + 1L;
/*     */     
/*  36 */     if (!this.displayedIntro && this.gameModeTicks > 20) {
/*  37 */       this.displayedIntro = true;
/*  38 */       this.player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 0.0F));
/*     */     } 
/*     */     
/*  41 */     this.demoHasEnded = (time > 120500L);
/*  42 */     if (this.demoHasEnded) {
/*  43 */       this.demoEndedReminder++;
/*     */     }
/*     */     
/*  46 */     if (time % 24000L == 500L) {
/*  47 */       if (day <= 6L) {
/*  48 */         if (day == 6L) {
/*  49 */           this.player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 104.0F));
/*     */         } else {
/*  51 */           this.player.sendSystemMessage(Component.translatable("demo.day." + day));
/*     */         } 
/*     */       }
/*  54 */     } else if (day == 1L) {
/*  55 */       if (time == 100L) {
/*  56 */         this.player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 101.0F));
/*  57 */       } else if (time == 175L) {
/*  58 */         this.player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 102.0F));
/*  59 */       } else if (time == 250L) {
/*  60 */         this.player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 103.0F));
/*     */       } 
/*  62 */     } else if (day == 5L && 
/*  63 */       time % 24000L == 22000L) {
/*  64 */       this.player.sendSystemMessage(Component.translatable("demo.day.warning"));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void outputDemoReminder() {
/*  70 */     if (this.demoEndedReminder > 100) {
/*  71 */       this.player.sendSystemMessage(Component.translatable("demo.reminder"));
/*  72 */       this.demoEndedReminder = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleBlockBreakAction(BlockPos pos, ServerboundPlayerActionPacket.Action action, Direction direction, int maxY, int sequence) {
/*  78 */     if (this.demoHasEnded) {
/*  79 */       outputDemoReminder();
/*     */       return;
/*     */     } 
/*  82 */     super.handleBlockBreakAction(pos, action, direction, maxY, sequence);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useItem(ServerPlayer player, Level level, ItemStack itemStack, InteractionHand hand) {
/*  87 */     if (this.demoHasEnded) {
/*  88 */       outputDemoReminder();
/*  89 */       return InteractionResult.PASS;
/*     */     } 
/*  91 */     return super.useItem(player, level, itemStack, hand);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useItemOn(ServerPlayer player, Level level, ItemStack itemStack, InteractionHand hand, BlockHitResult hitResult) {
/*  96 */     if (this.demoHasEnded) {
/*  97 */       outputDemoReminder();
/*  98 */       return InteractionResult.PASS;
/*     */     } 
/* 100 */     return super.useItemOn(player, level, itemStack, hand, hitResult);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\DemoMode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */