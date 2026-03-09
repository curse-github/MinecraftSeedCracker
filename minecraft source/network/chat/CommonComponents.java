/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ 
/*    */ public class CommonComponents {
/*  7 */   public static final Component EMPTY = Component.empty();
/*    */   
/*  9 */   public static final Component OPTION_ON = Component.translatable("options.on");
/* 10 */   public static final Component OPTION_OFF = Component.translatable("options.off");
/*    */   
/* 12 */   public static final Component GUI_DONE = Component.translatable("gui.done");
/* 13 */   public static final Component GUI_CANCEL = Component.translatable("gui.cancel");
/* 14 */   public static final Component GUI_YES = Component.translatable("gui.yes");
/* 15 */   public static final Component GUI_NO = Component.translatable("gui.no");
/* 16 */   public static final Component GUI_OK = Component.translatable("gui.ok");
/* 17 */   public static final Component GUI_PROCEED = Component.translatable("gui.proceed");
/* 18 */   public static final Component GUI_CONTINUE = Component.translatable("gui.continue");
/* 19 */   public static final Component GUI_BACK = Component.translatable("gui.back");
/* 20 */   public static final Component GUI_TO_TITLE = Component.translatable("gui.toTitle");
/* 21 */   public static final Component GUI_ACKNOWLEDGE = Component.translatable("gui.acknowledge");
/* 22 */   public static final Component GUI_OPEN_IN_BROWSER = Component.translatable("chat.link.open");
/* 23 */   public static final Component GUI_COPY_TO_CLIPBOARD = Component.translatable("chat.copy");
/* 24 */   public static final Component GUI_COPY_LINK_TO_CLIPBOARD = Component.translatable("gui.copy_link_to_clipboard");
/* 25 */   public static final Component GUI_DISCONNECT = Component.translatable("menu.disconnect");
/* 26 */   public static final Component GUI_RETURN_TO_MENU = Component.translatable("menu.returnToMenu");
/*    */   
/* 28 */   public static final Component TRANSFER_CONNECT_FAILED = Component.translatable("connect.failed.transfer");
/* 29 */   public static final Component CONNECT_FAILED = Component.translatable("connect.failed");
/*    */   
/* 31 */   public static final Component NEW_LINE = Component.literal("\n");
/* 32 */   public static final Component NARRATION_SEPARATOR = Component.literal(". ");
/*    */   
/* 34 */   public static final Component ELLIPSIS = Component.literal("...");
/* 35 */   public static final Component SPACE = space();
/*    */ 
/*    */   
/* 38 */   public static MutableComponent space() { return Component.literal(" "); }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public static MutableComponent days(long value) { return Component.translatable("gui.days", new Object[] { Long.valueOf(value) }); }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public static MutableComponent hours(long value) { return Component.translatable("gui.hours", new Object[] { Long.valueOf(value) }); }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public static MutableComponent minutes(long value) { return Component.translatable("gui.minutes", new Object[] { Long.valueOf(value) }); }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public static Component optionStatus(boolean value) { return value ? OPTION_ON : OPTION_OFF; }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public static Component disconnectButtonLabel(boolean isLocalServer) { return isLocalServer ? GUI_RETURN_TO_MENU : GUI_DISCONNECT; }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public static MutableComponent optionStatus(Component name, boolean value) { return Component.translatable(value ? "options.on.composed" : "options.off.composed", new Object[] { name }); }
/*    */ 
/*    */ 
/*    */   
/* 66 */   public static MutableComponent optionNameValue(Component name, Component value) { return Component.translatable("options.generic_value", new Object[] { name, value }); }
/*    */ 
/*    */   
/*    */   public static MutableComponent joinForNarration(Component... components) {
/* 70 */     MutableComponent result = Component.empty();
/* 71 */     for (int i = 0; i < components.length; i++) {
/* 72 */       result.append(components[i]);
/* 73 */       if (i != components.length - 1) {
/* 74 */         result.append(NARRATION_SEPARATOR);
/*    */       }
/*    */     } 
/* 77 */     return result;
/*    */   }
/*    */ 
/*    */   
/* 81 */   public static Component joinLines(Component... lines) { return joinLines(Arrays.asList(lines)); }
/*    */ 
/*    */ 
/*    */   
/* 85 */   public static Component joinLines(Collection<? extends Component> lines) { return ComponentUtils.formatList(lines, NEW_LINE); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\CommonComponents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */