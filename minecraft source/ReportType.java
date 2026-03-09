/*     */ package net.minecraft;
/*     */ 
/*     */ public final class ReportType extends Record {
/*     */   private final String header;
/*     */   private final List<String> nuggets;
/*     */   
/*   7 */   public ReportType(String header, List<String> nuggets) { this.header = header; this.nuggets = nuggets; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/ReportType;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*   7 */     //   0	7	0	this	Lnet/minecraft/ReportType; } public String header() { return this.header; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/ReportType;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/ReportType; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/ReportType;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/ReportType;
/*   7 */     //   0	8	1	o	Ljava/lang/Object; } public List<String> nuggets() { return this.nuggets; }
/*   8 */   public static final ReportType CRASH = new ReportType("Minecraft Crash Report", 
/*     */       
/*  10 */       List.of(new String[] { 
/*     */           "Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", 
/*     */           "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!", "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn't worry myself about that.", 
/*     */           "I bet Cylons wouldn't have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!", "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", 
/*     */           "You're mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine." }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   public static final ReportType PROFILE = new ReportType("Minecraft Profiler Results", 
/*     */       
/*  50 */       List.of(new String[] { 
/*     */           "I'd Rather Be Surfing", "Shiny numbers!", "Am I not running fast enough? :(", "I'm working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!", "Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers", 
/*     */           "Now with the same numbers", "You should add flames to things, it makes them go faster!", "Do you feel the need for... optimization?", "*cracks redstone whip*", "Maybe if you treated it better then it'll have more motivation to work faster! Poor server." }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public static final ReportType TEST = new ReportType("Minecraft Test Report", 
/*     */       
/*  71 */       List.of("Don't mind me", "One day I will be a real crash!", "Booo! Haha, did I scare you?", "Help, I'm trapped in a report factory!", "Have I answered your question?", "No hugs here, sorry", "I Can't Believe It's Not A Crash Report!", "Where's the kaboom!?"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   public static final ReportType NETWORK_PROTOCOL_ERROR = new ReportType("Minecraft Network Protocol Error Report", 
/*     */       
/*  85 */       List.of(new String[] { 
/*     */           "0xBADF00D", "+'${`%&NO CARRIER", "Please insert The Internet CD #4", "Sabotage!", "Are you sure you are not moving wrongly?", "This time is not my fault, I promise!", "All lines are down!", "Maybe a shark bit some cable", "404", "I'm sorry, I don't speak that language", 
/*     */           "What we've got here is failure to communicate", "It's the tubes, they're clogged!", "Abort, Retry, Ignore?", "Could be worse, I guess", "Wait, was the last bit one or zero?", "Too many suspicious packets", "Don't worry, I'll be fine", "Maybe this time it will work!", "I heard pigeons are more reliable" }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public static final ReportType CHUNK_IO_ERROR = new ReportType("Minecraft Chunk IO Error Report", 
/*     */       
/* 110 */       List.of(new String[] { 
/*     */           "I have failed you!", "Let's not do it again...", "Worst magic trick ever!", "Remember to backup your worlds regularly", "Pirates stole your chunk!", "Ker-chunk!", "Ideally, this shouldn't be here", "Let's hope it wasn't anything important", "Computers were a mistake", "Welp", 
/*     */           "Not my proudest moment", "Who needs blocks in a block game, right?", "This chunk is no more...it has ceased to be...this is an EX-chunk", "loss.mca" }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getErrorComment() {
/*     */     try {
/* 131 */       return (String)this.nuggets.get((int)(Util.getNanos() % this.nuggets.size()));
/* 132 */     } catch (Throwable ignored) {
/* 133 */       return "Witty comment unavailable :(";
/*     */     } 
/*     */   }
/*     */   
/*     */   public void appendHeader(StringBuilder builder, List<String> extraComments) {
/* 138 */     builder.append("---- ");
/* 139 */     builder.append(header());
/* 140 */     builder.append(" ----\n");
/* 141 */     builder.append("// ");
/* 142 */     builder.append(getErrorComment());
/* 143 */     builder.append('\n');
/* 144 */     for (String extraComment : extraComments) {
/* 145 */       builder.append("// ");
/* 146 */       builder.append(extraComment);
/* 147 */       builder.append('\n');
/*     */     } 
/* 149 */     builder.append('\n');
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\ReportType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */