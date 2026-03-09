/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.network.FilteredText;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.SignBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SignBlockEntity
/*     */   extends BlockEntity {
/*  39 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MAX_TEXT_LINE_WIDTH = 90;
/*     */   
/*     */   private static final int TEXT_LINE_HEIGHT = 10;
/*     */   private static final boolean DEFAULT_IS_WAXED = false;
/*     */   private UUID playerWhoMayEdit;
/*     */   private SignText frontText;
/*     */   private SignText backText;
/*     */   private boolean isWaxed = false;
/*     */   
/*  50 */   public SignBlockEntity(BlockPos worldPosition, BlockState blockState) { this(BlockEntityType.SIGN, worldPosition, blockState); }
/*     */ 
/*     */   
/*     */   public SignBlockEntity(BlockEntityType type, BlockPos worldPosition, BlockState blockState) {
/*  54 */     super(type, worldPosition, blockState);
/*  55 */     this.frontText = createDefaultSignText();
/*  56 */     this.backText = createDefaultSignText();
/*     */   }
/*     */ 
/*     */   
/*  60 */   protected SignText createDefaultSignText() { return new SignText(); }
/*     */ 
/*     */   
/*     */   public boolean isFacingFrontText(Player player) {
/*  64 */     Block block = getBlockState().getBlock(); if (block instanceof SignBlock) { SignBlock sign = (SignBlock)block;
/*  65 */       Vec3 signPositionOffset = sign.getSignHitboxCenterPosition(getBlockState());
/*  66 */       double xd = player.getX() - getBlockPos().getX() + signPositionOffset.x;
/*  67 */       double zd = player.getZ() - getBlockPos().getZ() + signPositionOffset.z;
/*     */       
/*  69 */       float signYRot = sign.getYRotationDegrees(getBlockState());
/*  70 */       float playerYRot = (float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F;
/*  71 */       return (Mth.degreesDifferenceAbs(signYRot, playerYRot) <= 90.0F); }
/*     */     
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */   
/*  77 */   public SignText getText(boolean isFrontText) { return isFrontText ? this.frontText : this.backText; }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public SignText getFrontText() { return this.frontText; }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public SignText getBackText() { return this.backText; }
/*     */ 
/*     */ 
/*     */   
/*  89 */   public int getTextLineHeight() { return 10; }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public int getMaxTextLineWidth() { return 90; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  98 */     super.saveAdditional(output);
/*     */     
/* 100 */     output.store("front_text", SignText.DIRECT_CODEC, this.frontText);
/* 101 */     output.store("back_text", SignText.DIRECT_CODEC, this.backText);
/* 102 */     output.putBoolean("is_waxed", this.isWaxed);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 107 */     super.loadAdditional(input);
/*     */     
/* 109 */     this.frontText = (SignText)input.read("front_text", SignText.DIRECT_CODEC).map(this::loadLines).orElseGet(SignText::new);
/* 110 */     this.backText = (SignText)input.read("back_text", SignText.DIRECT_CODEC).map(this::loadLines).orElseGet(SignText::new);
/* 111 */     this.isWaxed = input.getBooleanOr("is_waxed", false);
/*     */   }
/*     */   
/*     */   private SignText loadLines(SignText data) {
/* 115 */     for (int i = 0; i < 4; i++) {
/* 116 */       Component unfilteredMessage = loadLine(data.getMessage(i, false));
/* 117 */       Component filteredMessage = loadLine(data.getMessage(i, true));
/* 118 */       data = data.setMessage(i, unfilteredMessage, filteredMessage);
/*     */     } 
/* 120 */     return data;
/*     */   }
/*     */   
/*     */   private Component loadLine(Component component) {
/* 124 */     level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*     */       try {
/* 126 */         return ComponentUtils.updateForEntity(createCommandSourceStack(null, serverLevel, this.worldPosition), component, null, 0);
/* 127 */       } catch (CommandSyntaxException level) {} }
/*     */ 
/*     */     
/* 130 */     return component;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateSignText(Player player, boolean frontText, List<FilteredText> lines) {
/* 135 */     if (isWaxed() || !player.getUUID().equals(getPlayerWhoMayEdit()) || this.level == null) {
/* 136 */       LOGGER.warn("Player {} just tried to change non-editable sign", player.getPlainTextName());
/*     */       
/*     */       return;
/*     */     } 
/* 140 */     updateText(text -> setMessages(player, lines, text), frontText);
/* 141 */     setAllowedPlayerEditor(null);
/* 142 */     this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
/*     */   }
/*     */   
/*     */   public boolean updateText(UnaryOperator<SignText> function, boolean isFrontText) {
/* 146 */     SignText text = getText(isFrontText);
/* 147 */     return setText((SignText)function.apply(text), isFrontText);
/*     */   }
/*     */   
/*     */   private SignText setMessages(Player player, List<FilteredText> lines, SignText text) {
/* 151 */     for (int i = 0; i < lines.size(); i++) {
/* 152 */       FilteredText line = (FilteredText)lines.get(i);
/* 153 */       Style currentTextStyle = text.getMessage(i, player.isTextFilteringEnabled()).getStyle();
/* 154 */       if (player.isTextFilteringEnabled()) {
/*     */         
/* 156 */         text = text.setMessage(i, Component.literal(line.filteredOrEmpty()).setStyle(currentTextStyle));
/*     */       } else {
/* 158 */         text = text.setMessage(i, Component.literal(line.raw()).setStyle(currentTextStyle), Component.literal(line.filteredOrEmpty()).setStyle(currentTextStyle));
/*     */       } 
/*     */     } 
/* 161 */     return text;
/*     */   }
/*     */ 
/*     */   
/* 165 */   public boolean setText(SignText text, boolean isFrontText) { return isFrontText ? setFrontText(text) : setBackText(text); }
/*     */ 
/*     */   
/*     */   private boolean setBackText(SignText text) {
/* 169 */     if (text != this.backText) {
/* 170 */       this.backText = text;
/* 171 */       markUpdated();
/* 172 */       return true;
/*     */     } 
/* 174 */     return false;
/*     */   }
/*     */   
/*     */   private boolean setFrontText(SignText text) {
/* 178 */     if (text != this.frontText) {
/* 179 */       this.frontText = text;
/* 180 */       markUpdated();
/* 181 */       return true;
/*     */     } 
/* 183 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 187 */   public boolean canExecuteClickCommands(boolean isFrontText, Player player) { return (isWaxed() && getText(isFrontText).hasAnyClickCommands(player)); }
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
/*     */   public boolean executeClickCommandsIfPresent(ServerLevel level, Player player, BlockPos pos, boolean isFrontText) { // Byte code:
/*     */     //   0: iconst_0
/*     */     //   1: istore #5
/*     */     //   3: aload_0
/*     */     //   4: iload #4
/*     */     //   6: invokevirtual getText : (Z)Lnet/minecraft/world/level/block/entity/SignText;
/*     */     //   9: aload_2
/*     */     //   10: invokevirtual isTextFilteringEnabled : ()Z
/*     */     //   13: invokevirtual getMessages : (Z)[Lnet/minecraft/network/chat/Component;
/*     */     //   16: astore #6
/*     */     //   18: aload #6
/*     */     //   20: arraylength
/*     */     //   21: istore #7
/*     */     //   23: iconst_0
/*     */     //   24: istore #8
/*     */     //   26: iload #8
/*     */     //   28: iload #7
/*     */     //   30: if_icmpge -> 196
/*     */     //   33: aload #6
/*     */     //   35: iload #8
/*     */     //   37: aaload
/*     */     //   38: astore #9
/*     */     //   40: aload #9
/*     */     //   42: invokeinterface getStyle : ()Lnet/minecraft/network/chat/Style;
/*     */     //   47: astore #10
/*     */     //   49: aload #10
/*     */     //   51: invokevirtual getClickEvent : ()Lnet/minecraft/network/chat/ClickEvent;
/*     */     //   54: astore #11
/*     */     //   56: aload #11
/*     */     //   58: astore #12
/*     */     //   60: iconst_0
/*     */     //   61: istore #13
/*     */     //   63: aload #12
/*     */     //   65: iload #13
/*     */     //   67: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   72: tableswitch default -> 190, -1 -> 190, 0 -> 104, 1 -> 138, 2 -> 160
/*     */     //   104: aload #12
/*     */     //   106: checkcast net/minecraft/network/chat/ClickEvent$RunCommand
/*     */     //   109: astore #14
/*     */     //   111: aload_1
/*     */     //   112: invokevirtual getServer : ()Lnet/minecraft/server/MinecraftServer;
/*     */     //   115: invokevirtual getCommands : ()Lnet/minecraft/commands/Commands;
/*     */     //   118: aload_2
/*     */     //   119: aload_1
/*     */     //   120: aload_3
/*     */     //   121: invokestatic createCommandSourceStack : (Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/commands/CommandSourceStack;
/*     */     //   124: aload #14
/*     */     //   126: invokevirtual command : ()Ljava/lang/String;
/*     */     //   129: invokevirtual performPrefixedCommand : (Lnet/minecraft/commands/CommandSourceStack;Ljava/lang/String;)V
/*     */     //   132: iconst_1
/*     */     //   133: istore #5
/*     */     //   135: goto -> 190
/*     */     //   138: aload #12
/*     */     //   140: checkcast net/minecraft/network/chat/ClickEvent$ShowDialog
/*     */     //   143: astore #15
/*     */     //   145: aload_2
/*     */     //   146: aload #15
/*     */     //   148: invokevirtual dialog : ()Lnet/minecraft/core/Holder;
/*     */     //   151: invokevirtual openDialog : (Lnet/minecraft/core/Holder;)V
/*     */     //   154: iconst_1
/*     */     //   155: istore #5
/*     */     //   157: goto -> 190
/*     */     //   160: aload #12
/*     */     //   162: checkcast net/minecraft/network/chat/ClickEvent$Custom
/*     */     //   165: astore #16
/*     */     //   167: aload_1
/*     */     //   168: invokevirtual getServer : ()Lnet/minecraft/server/MinecraftServer;
/*     */     //   171: aload #16
/*     */     //   173: invokevirtual id : ()Lnet/minecraft/resources/Identifier;
/*     */     //   176: aload #16
/*     */     //   178: invokevirtual payload : ()Ljava/util/Optional;
/*     */     //   181: invokevirtual handleCustomClickAction : (Lnet/minecraft/resources/Identifier;Ljava/util/Optional;)V
/*     */     //   184: iconst_1
/*     */     //   185: istore #5
/*     */     //   187: goto -> 190
/*     */     //   190: iinc #8, 1
/*     */     //   193: goto -> 26
/*     */     //   196: iload #5
/*     */     //   198: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #191	-> 0
/*     */     //   #192	-> 3
/*     */     //   #193	-> 40
/*     */     //   #194	-> 49
/*     */     //   #195	-> 56
/*     */     //   #196	-> 104
/*     */     //   #197	-> 111
/*     */     //   #198	-> 132
/*     */     //   #199	-> 135
/*     */     //   #200	-> 138
/*     */     //   #201	-> 145
/*     */     //   #202	-> 154
/*     */     //   #203	-> 157
/*     */     //   #204	-> 160
/*     */     //   #205	-> 167
/*     */     //   #206	-> 184
/*     */     //   #207	-> 187
/*     */     //   #192	-> 190
/*     */     //   #212	-> 196
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   111	27	14	command	Lnet/minecraft/network/chat/ClickEvent$RunCommand;
/*     */     //   145	15	15	dialog	Lnet/minecraft/network/chat/ClickEvent$ShowDialog;
/*     */     //   167	23	16	custom	Lnet/minecraft/network/chat/ClickEvent$Custom;
/*     */     //   49	141	10	style	Lnet/minecraft/network/chat/Style;
/*     */     //   56	134	11	event	Lnet/minecraft/network/chat/ClickEvent;
/*     */     //   40	150	9	message	Lnet/minecraft/network/chat/Component;
/*     */     //   0	199	0	this	Lnet/minecraft/world/level/block/entity/SignBlockEntity;
/*     */     //   0	199	1	level	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   0	199	2	player	Lnet/minecraft/world/entity/player/Player;
/*     */     //   0	199	3	pos	Lnet/minecraft/core/BlockPos;
/*     */     //   0	199	4	isFrontText	Z
/*     */     //   3	196	5	hasAnyClickCommand	Z }
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
/*     */   private static CommandSourceStack createCommandSourceStack(Player player, ServerLevel level, BlockPos pos) {
/* 216 */     String textName = (player == null) ? "Sign" : player.getPlainTextName();
/* 217 */     MutableComponent mutableComponent = (player == null) ? Component.literal("Sign") : player.getDisplayName();
/* 218 */     return new CommandSourceStack(CommandSource.NULL, Vec3.atCenterOf(pos), Vec2.ZERO, level, LevelBasedPermissionSet.GAMEMASTER, textName, mutableComponent, level.getServer(), player);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 223 */   public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 228 */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveCustomOnly(registries); }
/*     */ 
/*     */ 
/*     */   
/* 232 */   public void setAllowedPlayerEditor(UUID playerUUID) { this.playerWhoMayEdit = playerUUID; }
/*     */ 
/*     */ 
/*     */   
/* 236 */   public UUID getPlayerWhoMayEdit() { return this.playerWhoMayEdit; }
/*     */ 
/*     */   
/*     */   private void markUpdated() {
/* 240 */     setChanged();
/* 241 */     this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
/*     */   }
/*     */ 
/*     */   
/* 245 */   public boolean isWaxed() { return this.isWaxed; }
/*     */ 
/*     */   
/*     */   public boolean setWaxed(boolean isWaxed) {
/* 249 */     if (this.isWaxed != isWaxed) {
/* 250 */       this.isWaxed = isWaxed;
/* 251 */       markUpdated();
/* 252 */       return true;
/*     */     } 
/* 254 */     return false;
/*     */   }
/*     */   
/*     */   public boolean playerIsTooFarAwayToEdit(UUID player) {
/* 258 */     Player editingPlayer = this.level.getPlayerByUUID(player);
/* 259 */     return (editingPlayer == null || !editingPlayer.isWithinBlockInteractionRange(getBlockPos(), 4.0D));
/*     */   }
/*     */   
/*     */   public static void tick(Level level, BlockPos blockPos, BlockState blockState, SignBlockEntity signBlockEntity) {
/* 263 */     UUID playerWhoMayEdit = signBlockEntity.getPlayerWhoMayEdit();
/* 264 */     if (playerWhoMayEdit != null) {
/* 265 */       signBlockEntity.clearInvalidPlayerWhoMayEdit(signBlockEntity, level, playerWhoMayEdit);
/*     */     }
/*     */   }
/*     */   
/*     */   private void clearInvalidPlayerWhoMayEdit(SignBlockEntity signBlockEntity, Level level, UUID playerWhoMayEdit) {
/* 270 */     if (signBlockEntity.playerIsTooFarAwayToEdit(playerWhoMayEdit)) {
/* 271 */       signBlockEntity.setAllowedPlayerEditor(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 276 */   public SoundEvent getSignInteractionFailedSoundEvent() { return SoundEvents.WAXED_SIGN_INTERACT_FAIL; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\SignBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */