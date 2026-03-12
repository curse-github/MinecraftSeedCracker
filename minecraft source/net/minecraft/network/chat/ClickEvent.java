/*     */ package net.minecraft.network.chat;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.io.File;
/*     */ import java.net.URI;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public interface ClickEvent {
/*  21 */   public static final Codec<ClickEvent> CODEC = Action.CODEC.dispatch("action", ClickEvent::action, action -> action.codec);
/*     */   Action action();
/*     */   public static final class OpenUrl extends Record implements ClickEvent { private final URI uri;
/*     */     
/*  25 */     public OpenUrl(URI uri) { this.uri = uri; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$OpenUrl;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  25 */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenUrl; } public URI uri() { return this.uri; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$OpenUrl;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenUrl; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$OpenUrl;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenUrl;
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/*  26 */     public static final MapCodec<OpenUrl> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.UNTRUSTED_URI
/*  27 */           .fieldOf("url").forGetter(OpenUrl::uri))
/*  28 */         .apply(i, OpenUrl::new));
/*     */ 
/*     */ 
/*     */     
/*  32 */     public ClickEvent.Action action() { return ClickEvent.Action.OPEN_URL; } }
/*     */   
/*     */   public static final class OpenFile extends Record implements ClickEvent { private final String path;
/*     */     
/*  36 */     public OpenFile(String path) { this.path = path; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$OpenFile;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenFile; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$OpenFile;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenFile; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$OpenFile;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenFile;
/*  36 */       //   0	8	1	o	Ljava/lang/Object; } public String path() { return this.path; }
/*  37 */     public static final MapCodec<OpenFile> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.STRING
/*  38 */           .fieldOf("path").forGetter(OpenFile::path))
/*  39 */         .apply(i, OpenFile::new));
/*     */ 
/*     */     
/*  42 */     public OpenFile(File file) { this(file.toString()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  47 */     public OpenFile(Path path) { this(path.toFile()); }
/*     */ 
/*     */ 
/*     */     
/*  51 */     public File file() { return new File(this.path); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  56 */     public ClickEvent.Action action() { return ClickEvent.Action.OPEN_FILE; } }
/*     */   
/*     */   public static final class RunCommand extends Record implements ClickEvent { private final String command;
/*     */     
/*  60 */     public RunCommand(String command) { this.command = command; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$RunCommand;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$RunCommand; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$RunCommand;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$RunCommand; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$RunCommand;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$RunCommand;
/*  60 */       //   0	8	1	o	Ljava/lang/Object; } public String command() { return this.command; }
/*  61 */     public static final MapCodec<RunCommand> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.CHAT_STRING
/*  62 */           .fieldOf("command").forGetter(RunCommand::command))
/*  63 */         .apply(i, RunCommand::new));
/*     */ 
/*     */ 
/*     */     
/*  67 */     public ClickEvent.Action action() { return ClickEvent.Action.RUN_COMMAND; } }
/*     */   
/*     */   public static final class SuggestCommand extends Record implements ClickEvent { private final String command;
/*     */     
/*  71 */     public SuggestCommand(String command) { this.command = command; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$SuggestCommand;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$SuggestCommand; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$SuggestCommand;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$SuggestCommand; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$SuggestCommand;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$SuggestCommand;
/*  71 */       //   0	8	1	o	Ljava/lang/Object; } public String command() { return this.command; }
/*  72 */     public static final MapCodec<SuggestCommand> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.CHAT_STRING
/*  73 */           .fieldOf("command").forGetter(SuggestCommand::command))
/*  74 */         .apply(i, SuggestCommand::new));
/*     */ 
/*     */ 
/*     */     
/*  78 */     public ClickEvent.Action action() { return ClickEvent.Action.SUGGEST_COMMAND; } }
/*     */   
/*     */   public static final class ShowDialog extends Record implements ClickEvent { private final Holder<Dialog> dialog;
/*     */     
/*  82 */     public ShowDialog(Holder<Dialog> dialog) { this.dialog = dialog; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$ShowDialog;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #82	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$ShowDialog; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$ShowDialog;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #82	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$ShowDialog; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$ShowDialog;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #82	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$ShowDialog;
/*  82 */       //   0	8	1	o	Ljava/lang/Object; } public Holder<Dialog> dialog() { return this.dialog; }
/*  83 */     public static final MapCodec<ShowDialog> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Dialog.CODEC
/*  84 */           .fieldOf("dialog").forGetter(ShowDialog::dialog))
/*  85 */         .apply(i, ShowDialog::new));
/*     */ 
/*     */ 
/*     */     
/*  89 */     public ClickEvent.Action action() { return ClickEvent.Action.SHOW_DIALOG; } }
/*     */   
/*     */   public static final class ChangePage extends Record implements ClickEvent { private final int page;
/*     */     
/*  93 */     public ChangePage(int page) { this.page = page; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$ChangePage;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$ChangePage; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$ChangePage;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$ChangePage; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$ChangePage;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$ChangePage;
/*  93 */       //   0	8	1	o	Ljava/lang/Object; } public int page() { return this.page; }
/*  94 */     public static final MapCodec<ChangePage> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.POSITIVE_INT
/*  95 */           .fieldOf("page").forGetter(ChangePage::page))
/*  96 */         .apply(i, ChangePage::new));
/*     */ 
/*     */ 
/*     */     
/* 100 */     public ClickEvent.Action action() { return ClickEvent.Action.CHANGE_PAGE; } }
/*     */   
/*     */   public static final class CopyToClipboard extends Record implements ClickEvent { private final String value;
/*     */     
/* 104 */     public CopyToClipboard(String value) { this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$CopyToClipboard;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$CopyToClipboard; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$CopyToClipboard;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$CopyToClipboard; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$CopyToClipboard;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$CopyToClipboard;
/* 104 */       //   0	8	1	o	Ljava/lang/Object; } public String value() { return this.value; }
/* 105 */     public static final MapCodec<CopyToClipboard> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.STRING
/* 106 */           .fieldOf("value").forGetter(CopyToClipboard::value))
/* 107 */         .apply(i, CopyToClipboard::new));
/*     */ 
/*     */ 
/*     */     
/* 111 */     public ClickEvent.Action action() { return ClickEvent.Action.COPY_TO_CLIPBOARD; } }
/*     */   public static final class Custom extends Record implements ClickEvent { private final Identifier id;
/*     */     private final Optional<Tag> payload;
/*     */     
/* 115 */     public Custom(Identifier id, Optional<Tag> payload) { this.id = id; this.payload = payload; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$Custom;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$Custom; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$Custom;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$Custom; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$Custom;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$Custom;
/* 115 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier id() { return this.id; } public Optional<Tag> payload() { return this.payload; }
/* 116 */     public static final MapCodec<Custom> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC
/* 117 */           .fieldOf("id").forGetter(Custom::id), ExtraCodecs.NBT
/*     */           
/* 119 */           .optionalFieldOf("payload").forGetter(Custom::payload))
/* 120 */         .apply(i, Custom::new));
/*     */ 
/*     */ 
/*     */     
/* 124 */     public ClickEvent.Action action() { return ClickEvent.Action.CUSTOM; } }
/*     */ 
/*     */   
/*     */   public enum Action
/*     */     implements StringRepresentable {
/* 129 */     OPEN_URL("open_url", true, ClickEvent.OpenUrl.CODEC),
/* 130 */     OPEN_FILE("open_file", false, ClickEvent.OpenFile.CODEC),
/* 131 */     RUN_COMMAND("run_command", true, ClickEvent.RunCommand.CODEC),
/* 132 */     SUGGEST_COMMAND("suggest_command", true, ClickEvent.SuggestCommand.CODEC),
/* 133 */     SHOW_DIALOG("show_dialog", true, ClickEvent.ShowDialog.CODEC),
/* 134 */     CHANGE_PAGE("change_page", true, ClickEvent.ChangePage.CODEC),
/* 135 */     COPY_TO_CLIPBOARD("copy_to_clipboard", true, ClickEvent.CopyToClipboard.CODEC),
/* 136 */     CUSTOM("custom", true, ClickEvent.Custom.CODEC); public static final Codec<Action> UNSAFE_CODEC; public static final Codec<Action> CODEC; private final boolean allowFromServer; private final String name; private final MapCodec<? extends ClickEvent> codec;
/*     */     
/*     */     static  {
/* 139 */       UNSAFE_CODEC = StringRepresentable.fromEnum(Action::values);
/* 140 */       CODEC = UNSAFE_CODEC.validate(Action::filterForSerialization);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Action(String name, boolean allowFromServer, MapCodec<? extends ClickEvent> codec) {
/* 147 */       this.name = name;
/* 148 */       this.allowFromServer = allowFromServer;
/* 149 */       this.codec = codec;
/*     */     }
/*     */ 
/*     */     
/* 153 */     public boolean isAllowedFromServer() { return this.allowFromServer; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/* 162 */     public MapCodec<? extends ClickEvent> valueCodec() { return this.codec; }
/*     */ 
/*     */     
/*     */     public static DataResult<Action> filterForSerialization(Action action) {
/* 166 */       if (!action.isAllowedFromServer()) {
/* 167 */         return DataResult.error(() -> "Click event type not allowed: " + String.valueOf(action));
/*     */       }
/* 169 */       return DataResult.success(action, Lifecycle.stable());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\ClickEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */