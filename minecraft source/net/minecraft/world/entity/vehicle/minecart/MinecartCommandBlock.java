/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BaseCommandBlock;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class MinecartCommandBlock
/*     */   extends AbstractMinecart {
/*  28 */   private static final EntityDataAccessor<String> DATA_ID_COMMAND_NAME = SynchedEntityData.defineId(MinecartCommandBlock.class, EntityDataSerializers.STRING);
/*  29 */   private static final EntityDataAccessor<Component> DATA_ID_LAST_OUTPUT = SynchedEntityData.defineId(MinecartCommandBlock.class, EntityDataSerializers.COMPONENT);
/*     */   
/*  31 */   private final BaseCommandBlock commandBlock = new MinecartCommandBase();
/*     */   
/*     */   private static final int ACTIVATION_DELAY = 4;
/*     */   
/*     */   private int lastActivated;
/*     */   
/*  37 */   public MinecartCommandBlock(EntityType<? extends MinecartCommandBlock> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   protected Item getDropItem() { return Items.MINECART; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public ItemStack getPickResult() { return new ItemStack(Items.COMMAND_BLOCK_MINECART); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  52 */     super.defineSynchedData(entityData);
/*  53 */     entityData.define(DATA_ID_COMMAND_NAME, "");
/*  54 */     entityData.define(DATA_ID_LAST_OUTPUT, CommonComponents.EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  59 */     super.readAdditionalSaveData(input);
/*  60 */     this.commandBlock.load(input);
/*  61 */     getEntityData().set(DATA_ID_COMMAND_NAME, getCommandBlock().getCommand());
/*  62 */     getEntityData().set(DATA_ID_LAST_OUTPUT, getCommandBlock().getLastOutput());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  67 */     super.addAdditionalSaveData(output);
/*  68 */     this.commandBlock.save(output);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public BlockState getDefaultDisplayBlockState() { return Blocks.COMMAND_BLOCK.defaultBlockState(); }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public BaseCommandBlock getCommandBlock() { return this.commandBlock; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateMinecart(ServerLevel level, int xt, int yt, int zt, boolean state) {
/*  82 */     if (state && 
/*  83 */       this.tickCount - this.lastActivated >= 4) {
/*  84 */       getCommandBlock().performCommand(level);
/*  85 */       this.lastActivated = this.tickCount;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand) {
/*  92 */     if (!player.canUseGameMasterBlocks()) {
/*  93 */       return InteractionResult.PASS;
/*     */     }
/*  95 */     if (player.level().isClientSide()) {
/*  96 */       player.openMinecartCommandBlock(this);
/*     */     }
/*  98 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 103 */     super.onSyncedDataUpdated(accessor);
/*     */     
/* 105 */     if (DATA_ID_LAST_OUTPUT.equals(accessor)) {
/*     */       try {
/* 107 */         this.commandBlock.setLastOutput((Component)getEntityData().get(DATA_ID_LAST_OUTPUT));
/* 108 */       } catch (Throwable throwable) {}
/*     */     }
/* 110 */     else if (DATA_ID_COMMAND_NAME.equals(accessor)) {
/* 111 */       this.commandBlock.setCommand((String)getEntityData().get(DATA_ID_COMMAND_NAME));
/*     */     } 
/*     */   }
/*     */   
/*     */   private class MinecartCommandBase
/*     */     extends BaseCommandBlock {
/*     */     public void onUpdated(ServerLevel level) {
/* 118 */       MinecartCommandBlock.this.getEntityData().set(MinecartCommandBlock.DATA_ID_COMMAND_NAME, getCommand());
/* 119 */       MinecartCommandBlock.this.getEntityData().set(MinecartCommandBlock.DATA_ID_LAST_OUTPUT, getLastOutput());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 124 */     public CommandSourceStack createCommandSourceStack(ServerLevel level, CommandSource source) { return new CommandSourceStack(source, MinecartCommandBlock.this.position(), MinecartCommandBlock.this.getRotationVector(), level, LevelBasedPermissionSet.GAMEMASTER, getName().getString(), MinecartCommandBlock.this.getDisplayName(), level.getServer(), MinecartCommandBlock.this); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     public boolean isValid() { return !MinecartCommandBlock.this.isRemoved(); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartCommandBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */