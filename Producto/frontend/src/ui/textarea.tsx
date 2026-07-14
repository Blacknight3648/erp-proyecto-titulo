import * as React from "react";

import { cn } from "./utils";

function Textarea({ className, ...props }: React.ComponentProps<"textarea">) {
  return (
    <textarea
      data-slot="textarea"
      className={cn(
        "placeholder:text-muted-foreground aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive flex min-h-16 w-full rounded-xl border border-gray-200 dark:border-border bg-white dark:bg-input-background px-4 py-2 text-sm transition-[border-color,box-shadow] outline-none disabled:cursor-not-allowed disabled:opacity-50",
        "focus:border-primary focus:ring-2 focus:ring-ring/25 focus-visible:outline-none",
        className,
      )}
      {...props}
    />
  );
}

export { Textarea };
