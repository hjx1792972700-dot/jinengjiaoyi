export function inspect(obj) {
  return JSON.stringify(obj, null, 2)
}
export function format(...args) {
  return args.join(' ')
}
export function debuglog() {
  return function() {}
}
export function deprecate(fn) {
  return fn
}
export default { inspect, format, debuglog, deprecate }
