/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.world.level.BaseCommandBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CommandBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class CommandBlockEntity
/*     */   extends BlockEntity
/*     */ {
/*     */   private static final boolean DEFAULT_POWERED = false;
/*     */   private static final boolean DEFAULT_CONDITION_MET = false;
/*     */   private static final boolean DEFAULT_AUTOMATIC = false;
/*     */   
/*     */   public CommandBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  32 */     super(BlockEntityType.COMMAND_BLOCK, worldPosition, blockState);
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
/* 158 */     this.commandBlock = new BaseCommandBlock()
/*     */       {
/*     */         public void setCommand(String command) {
/* 161 */           super.setCommand(command);
/* 162 */           CommandBlockEntity.this.setChanged();
/*     */         }
/*     */ 
/*     */         
/*     */         public void onUpdated(ServerLevel level) {
/* 167 */           BlockState state = level.getBlockState(CommandBlockEntity.this.worldPosition);
/* 168 */           level.sendBlockUpdated(CommandBlockEntity.this.worldPosition, state, state, 3);
/*     */         }
/*     */ 
/*     */         
/*     */         public CommandSourceStack createCommandSourceStack(ServerLevel level, CommandSource source) {
/* 173 */           Direction facing = (Direction)CommandBlockEntity.this.getBlockState().getValue(CommandBlock.FACING);
/* 174 */           return new CommandSourceStack(source, Vec3.atCenterOf(CommandBlockEntity.this.worldPosition), new Vec2(0.0F, facing.toYRot()), level, LevelBasedPermissionSet.GAMEMASTER, getName().getString(), getName(), level.getServer(), null);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 179 */         public boolean isValid() { return !CommandBlockEntity.this.isRemoved(); }
/*     */       };
/*     */   }
/*     */   
/*     */   private boolean powered = false;
/*     */   private boolean auto = false;
/*     */   private boolean conditionMet = false;
/*     */   private final BaseCommandBlock commandBlock;
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*     */     super.saveAdditional(output);
/*     */     this.commandBlock.save(output);
/*     */     output.putBoolean("powered", isPowered());
/*     */     output.putBoolean("conditionMet", wasConditionMet());
/*     */     output.putBoolean("auto", isAutomatic());
/*     */   }
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*     */     super.loadAdditional(input);
/*     */     this.commandBlock.load(input);
/*     */     this.powered = input.getBooleanOr("powered", false);
/*     */     this.conditionMet = input.getBooleanOr("conditionMet", false);
/*     */     setAutomatic(input.getBooleanOr("auto", false));
/*     */   }
/*     */   
/*     */   public BaseCommandBlock getCommandBlock() { return this.commandBlock; }
/*     */   
/*     */   public void setPowered(boolean powered) { this.powered = powered; }
/*     */   
/*     */   public boolean isPowered() { return this.powered; }
/*     */   
/*     */   public boolean isAutomatic() { return this.auto; }
/*     */   
/*     */   public void setAutomatic(boolean auto) {
/*     */     boolean previousAuto = this.auto;
/*     */     this.auto = auto;
/*     */     if (!previousAuto && auto && !this.powered && this.level != null && getMode() != Mode.SEQUENCE)
/*     */       scheduleTick(); 
/*     */   }
/*     */   
/*     */   public void onModeSwitch() {
/*     */     Mode newMode = getMode();
/*     */     if (newMode == Mode.AUTO && (this.powered || this.auto) && this.level != null)
/*     */       scheduleTick(); 
/*     */   }
/*     */   
/*     */   private void scheduleTick() {
/*     */     Block commandBlock = getBlockState().getBlock();
/*     */     if (commandBlock instanceof CommandBlock) {
/*     */       markConditionMet();
/*     */       this.level.scheduleTick(this.worldPosition, commandBlock, 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean wasConditionMet() { return this.conditionMet; }
/*     */   
/*     */   public boolean markConditionMet() {
/*     */     this.conditionMet = true;
/*     */     if (isConditional()) {
/*     */       BlockPos relative = this.worldPosition.relative(((Direction)this.level.getBlockState(this.worldPosition).getValue(CommandBlock.FACING)).getOpposite());
/*     */       if (this.level.getBlockState(relative).getBlock() instanceof CommandBlock) {
/*     */         BlockEntity backsideCommandBlock = this.level.getBlockEntity(relative);
/*     */         this.conditionMet = (backsideCommandBlock instanceof CommandBlockEntity && ((CommandBlockEntity)backsideCommandBlock).getCommandBlock().getSuccessCount() > 0);
/*     */       } else {
/*     */         this.conditionMet = false;
/*     */       } 
/*     */     } 
/*     */     return this.conditionMet;
/*     */   }
/*     */   
/*     */   public Mode getMode() {
/*     */     BlockState state = getBlockState();
/*     */     if (state.is(Blocks.COMMAND_BLOCK))
/*     */       return Mode.REDSTONE; 
/*     */     if (state.is(Blocks.REPEATING_COMMAND_BLOCK))
/*     */       return Mode.AUTO; 
/*     */     if (state.is(Blocks.CHAIN_COMMAND_BLOCK))
/*     */       return Mode.SEQUENCE; 
/*     */     return Mode.REDSTONE;
/*     */   }
/*     */   
/*     */   public boolean isConditional() {
/*     */     BlockState blockState = this.level.getBlockState(getBlockPos());
/*     */     if (blockState.getBlock() instanceof CommandBlock)
/*     */       return ((Boolean)blockState.getValue(CommandBlock.CONDITIONAL)).booleanValue(); 
/*     */     return false;
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/*     */     super.applyImplicitComponents(components);
/*     */     this.commandBlock.setCustomName((Component)components.get(DataComponents.CUSTOM_NAME));
/*     */   }
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/*     */     super.collectImplicitComponents(components);
/*     */     components.set(DataComponents.CUSTOM_NAME, this.commandBlock.getCustomName());
/*     */   }
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput output) {
/*     */     super.removeComponentsFromTag(output);
/*     */     output.discard("CustomName");
/*     */     output.discard("conditionMet");
/*     */     output.discard("powered");
/*     */   }
/*     */   
/*     */   public enum Mode {
/*     */     SEQUENCE, AUTO, REDSTONE;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\CommandBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */