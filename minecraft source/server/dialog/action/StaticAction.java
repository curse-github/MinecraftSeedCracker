/*    */ package net.minecraft.server.dialog.action;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Collections;
/*    */ import java.util.EnumMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.network.chat.ClickEvent.Action;
/*    */ 
/*    */ public final class StaticAction extends Record implements Action {
/* 12 */   public StaticAction(ClickEvent value) { this.value = value; } private final ClickEvent value; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/action/StaticAction;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/StaticAction; } public ClickEvent value() { return this.value; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/action/StaticAction;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/StaticAction; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/action/StaticAction;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/action/StaticAction;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 15 */   public static final Map<ClickEvent.Action, MapCodec<StaticAction>> WRAPPED_CODECS = (Map)Util.make(() -> {
/* 16 */         result = new EnumMap(ClickEvent.Action.class);
/* 17 */         for (ClickEvent.Action action : (Action[])ClickEvent.Action.class.getEnumConstants()) {
/* 18 */           if (action.isAllowedFromServer()) {
/* 19 */             MapCodec<ClickEvent> mapCodec = action.valueCodec();
/* 20 */             result.put(action, mapCodec.xmap(StaticAction::new, StaticAction::value));
/*    */           } 
/*    */         } 
/* 23 */         return Collections.unmodifiableMap(result);
/*    */       });
/*    */ 
/*    */ 
/*    */   
/* 28 */   public MapCodec<StaticAction> codec() { return (MapCodec)WRAPPED_CODECS.get(this.value.action()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public Optional<ClickEvent> createAction(Map<String, Action.ValueGetter> parameters) { return Optional.of(this.value); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\action\StaticAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */