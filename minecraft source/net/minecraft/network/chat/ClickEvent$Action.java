/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.util.StringRepresentable;
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
/*     */ public static enum Action
/*     */   implements StringRepresentable
/*     */ {
/*     */   public static final Codec<Action> UNSAFE_CODEC;
/*     */   public static final Codec<Action> CODEC;
/* 129 */   OPEN_URL("open_url", true, ClickEvent.OpenUrl.CODEC),
/* 130 */   OPEN_FILE("open_file", false, ClickEvent.OpenFile.CODEC),
/* 131 */   RUN_COMMAND("run_command", true, ClickEvent.RunCommand.CODEC),
/* 132 */   SUGGEST_COMMAND("suggest_command", true, ClickEvent.SuggestCommand.CODEC),
/* 133 */   SHOW_DIALOG("show_dialog", true, ClickEvent.ShowDialog.CODEC),
/* 134 */   CHANGE_PAGE("change_page", true, ClickEvent.ChangePage.CODEC),
/* 135 */   COPY_TO_CLIPBOARD("copy_to_clipboard", true, ClickEvent.CopyToClipboard.CODEC),
/* 136 */   CUSTOM("custom", true, ClickEvent.Custom.CODEC);
/*     */   
/*     */   static  {
/* 139 */     UNSAFE_CODEC = StringRepresentable.fromEnum(Action::values);
/* 140 */     CODEC = UNSAFE_CODEC.validate(Action::filterForSerialization);
/*     */   }
/*     */ 
/*     */   
/*     */   private final boolean allowFromServer;
/*     */   
/*     */   Action(String name, boolean allowFromServer, MapCodec<? extends ClickEvent> codec) {
/* 147 */     this.name = name;
/* 148 */     this.allowFromServer = allowFromServer;
/* 149 */     this.codec = codec;
/*     */   }
/*     */   private final String name; private final MapCodec<? extends ClickEvent> codec;
/*     */   
/* 153 */   public boolean isAllowedFromServer() { return this.allowFromServer; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/* 162 */   public MapCodec<? extends ClickEvent> valueCodec() { return this.codec; }
/*     */ 
/*     */   
/*     */   public static DataResult<Action> filterForSerialization(Action action) {
/* 166 */     if (!action.isAllowedFromServer()) {
/* 167 */       return DataResult.error(() -> "Click event type not allowed: " + String.valueOf(action));
/*     */     }
/* 169 */     return DataResult.success(action, Lifecycle.stable());
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\ClickEvent$Action.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */