const validate = (input, { required, patterns, rules }) => {
  let hasError = false;
  let errorMessages = [];

  if (required?.value && input?.length === 0) {
    errorMessages.push(required?.message || "Campo obrigatório.");
    hasError = true;
  }

  patterns?.forEach((pattern) => {
    const regexp = new RegExp(pattern?.pattern, "g");

    if (!regexp.test(input)) {
      errorMessages.push(pattern?.message);
      hasError = true;
    }
  });

  rules?.forEach((rule) => {
    if (!rule?.rule(input)) {
      errorMessages.push(rule?.message);
      hasError = true;
    }
  });

  return {
    hasError,
    errorMessages,
  };
};

const validator = {
  validate,
};

export { validate };
export default validator;
