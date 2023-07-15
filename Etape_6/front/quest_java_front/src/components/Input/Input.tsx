import React from "react";

type Props = {
  inputLabel?: string;
  inputType: React.HTMLInputTypeAttribute;
  inputValue: string;
  isDisable?: boolean;
  onValueChange: (val: any) => void;
};

export const Input = ({
  inputLabel,
  inputType,
  inputValue,
  isDisable = false,
  onValueChange,
}: Props) => (
  <div className="flex flex-col my-2">
    {inputLabel && <label className="text-left">{inputLabel}</label>}
    <input
      type={inputType}
      value={inputValue}
      onChange={(e) => {
        onValueChange(e.target.value);
      }}
      disabled={isDisable}
      className="border border-gray-700 rounded-md h-8 mt-1 px-2"
    />
  </div>
);
