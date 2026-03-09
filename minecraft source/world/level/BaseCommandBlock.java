/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ public abstract class BaseCommandBlock
/*     */ {
/*  25 */   private static final Component DEFAULT_NAME = Component.literal("@");
/*     */   
/*     */   private static final int NO_LAST_EXECUTION = -1;
/*  28 */   private long lastExecution = -1L;
/*     */   private boolean updateLastExecution = true;
/*     */   private int successCount;
/*     */   private boolean trackOutput = true;
/*     */   private Component lastOutput;
/*  33 */   private String command = "";
/*     */   
/*     */   private Component customName;
/*     */   
/*  37 */   public int getSuccessCount() { return this.successCount; }
/*     */ 
/*     */ 
/*     */   
/*  41 */   public void setSuccessCount(int successCount) { this.successCount = successCount; }
/*     */ 
/*     */ 
/*     */   
/*  45 */   public Component getLastOutput() { return (this.lastOutput == null) ? CommonComponents.EMPTY : this.lastOutput; }
/*     */ 
/*     */   
/*     */   public void save(ValueOutput output) {
/*  49 */     output.putString("Command", this.command);
/*  50 */     output.putInt("SuccessCount", this.successCount);
/*  51 */     output.storeNullable("CustomName", ComponentSerialization.CODEC, this.customName);
/*  52 */     output.putBoolean("TrackOutput", this.trackOutput);
/*  53 */     if (this.trackOutput) {
/*  54 */       output.storeNullable("LastOutput", ComponentSerialization.CODEC, this.lastOutput);
/*     */     }
/*  56 */     output.putBoolean("UpdateLastExecution", this.updateLastExecution);
/*  57 */     if (this.updateLastExecution && this.lastExecution != -1L) {
/*  58 */       output.putLong("LastExecution", this.lastExecution);
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(ValueInput input) {
/*  63 */     this.command = input.getStringOr("Command", "");
/*  64 */     this.successCount = input.getIntOr("SuccessCount", 0);
/*  65 */     setCustomName(BlockEntity.parseCustomNameSafe(input, "CustomName"));
/*  66 */     this.trackOutput = input.getBooleanOr("TrackOutput", true);
/*  67 */     if (this.trackOutput) {
/*  68 */       this.lastOutput = BlockEntity.parseCustomNameSafe(input, "LastOutput");
/*     */     } else {
/*  70 */       this.lastOutput = null;
/*     */     } 
/*  72 */     this.updateLastExecution = input.getBooleanOr("UpdateLastExecution", true);
/*  73 */     if (this.updateLastExecution) {
/*  74 */       this.lastExecution = input.getLongOr("LastExecution", -1L);
/*     */     } else {
/*  76 */       this.lastExecution = -1L;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setCommand(String command) {
/*  81 */     this.command = command;
/*  82 */     this.successCount = 0;
/*     */   }
/*     */ 
/*     */   
/*  86 */   public String getCommand() { return this.command; }
/*     */ 
/*     */   
/*     */   public boolean performCommand(ServerLevel level) {
/*  90 */     if (level.getGameTime() == this.lastExecution) {
/*  91 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  95 */     if ("Searge".equalsIgnoreCase(this.command)) {
/*  96 */       this.lastOutput = Component.literal("#itzlipofutzli");
/*  97 */       this.successCount = 1;
/*  98 */       return true;
/*     */     } 
/*     */     
/* 101 */     this.successCount = 0;
/*     */     
/* 103 */     if (level.isCommandBlockEnabled() && !StringUtil.isNullOrEmpty(this.command)) {
/*     */       try {
/* 105 */         this.lastOutput = null;
/*     */         
/* 107 */         CloseableCommandBlockSource commandSource = createSource(level); 
/* 108 */         try { CommandSource effectiveCommandSource = (CommandSource)Objects.requireNonNullElse(commandSource, CommandSource.NULL);
/* 109 */           CommandSourceStack commandSourceStack = createCommandSourceStack(level, effectiveCommandSource).withCallback((success, result) -> {
/* 110 */                 if (success) {
/* 111 */                   this.successCount++;
/*     */                 }
/*     */               });
/* 114 */           level.getServer().getCommands().performPrefixedCommand(commandSourceStack, this.command);
/* 115 */           if (commandSource != null) commandSource.close();  } catch (Throwable throwable) { if (commandSource != null)
/*     */             try { commandSource.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; } 
/* 117 */       } catch (Throwable t) {
/* 118 */         CrashReport report = CrashReport.forThrowable(t, "Executing command block");
/* 119 */         CrashReportCategory category = report.addCategory("Command to be executed");
/*     */         
/* 121 */         category.setDetail("Command", this::getCommand);
/*     */         
/* 123 */         category.setDetail("Name", () -> getName().getString());
/*     */         
/* 125 */         throw new ReportedException(report);
/*     */       } 
/*     */     }
/*     */     
/* 129 */     if (this.updateLastExecution) {
/* 130 */       this.lastExecution = level.getGameTime();
/*     */     } else {
/* 132 */       this.lastExecution = -1L;
/*     */     } 
/*     */     
/* 135 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 139 */   private CloseableCommandBlockSource createSource(ServerLevel level) { return this.trackOutput ? new CloseableCommandBlockSource(level) : null; }
/*     */ 
/*     */ 
/*     */   
/* 143 */   public Component getName() { return (this.customName != null) ? this.customName : DEFAULT_NAME; }
/*     */ 
/*     */ 
/*     */   
/* 147 */   public Component getCustomName() { return this.customName; }
/*     */ 
/*     */ 
/*     */   
/* 151 */   public void setCustomName(Component name) { this.customName = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   public void setLastOutput(Component lastOutput) { this.lastOutput = lastOutput; }
/*     */ 
/*     */ 
/*     */   
/* 161 */   public void setTrackOutput(boolean trackOutput) { this.trackOutput = trackOutput; }
/*     */ 
/*     */ 
/*     */   
/* 165 */   public boolean isTrackOutput() { return this.trackOutput; }
/*     */   
/*     */   public abstract void onUpdated(ServerLevel paramServerLevel);
/*     */   
/*     */   public abstract CommandSourceStack createCommandSourceStack(ServerLevel paramServerLevel, CommandSource paramCommandSource);
/*     */   
/*     */   public abstract boolean isValid();
/*     */   
/*     */   protected class CloseableCommandBlockSource implements CommandSource, AutoCloseable { private final ServerLevel level;
/* 174 */     private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ROOT);
/*     */     
/*     */     private boolean closed;
/*     */ 
/*     */     
/* 179 */     protected CloseableCommandBlockSource(ServerLevel level) { this.level = level; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 184 */     public boolean acceptsSuccess() { return (!this.closed && ((Boolean)this.level.getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK)).booleanValue()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 189 */     public boolean acceptsFailure() { return !this.closed; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     public boolean shouldInformAdmins() { return (!this.closed && ((Boolean)this.level.getGameRules().get(GameRules.COMMAND_BLOCK_OUTPUT)).booleanValue()); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void sendSystemMessage(Component message) {
/* 199 */       if (!this.closed) {
/* 200 */         BaseCommandBlock.this.lastOutput = Component.literal("[" + TIME_FORMAT.format(ZonedDateTime.now()) + "] ").append(message);
/* 201 */         BaseCommandBlock.this.onUpdated(this.level);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 207 */     public void close() { this.closed = true; } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\BaseCommandBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */