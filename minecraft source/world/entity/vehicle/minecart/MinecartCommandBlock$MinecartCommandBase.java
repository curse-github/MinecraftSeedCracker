/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.world.level.BaseCommandBlock;
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
/*     */ class MinecartCommandBase
/*     */   extends BaseCommandBlock
/*     */ {
/*     */   public void onUpdated(ServerLevel level) {
/* 118 */     MinecartCommandBlock.this.getEntityData().set(MinecartCommandBlock.DATA_ID_COMMAND_NAME, getCommand());
/* 119 */     MinecartCommandBlock.this.getEntityData().set(MinecartCommandBlock.DATA_ID_LAST_OUTPUT, getLastOutput());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public CommandSourceStack createCommandSourceStack(ServerLevel level, CommandSource source) { return new CommandSourceStack(source, MinecartCommandBlock.this.position(), MinecartCommandBlock.this.getRotationVector(), level, LevelBasedPermissionSet.GAMEMASTER, getName().getString(), MinecartCommandBlock.this.getDisplayName(), level.getServer(), MinecartCommandBlock.this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public boolean isValid() { return !MinecartCommandBlock.this.isRemoved(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartCommandBlock$MinecartCommandBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */